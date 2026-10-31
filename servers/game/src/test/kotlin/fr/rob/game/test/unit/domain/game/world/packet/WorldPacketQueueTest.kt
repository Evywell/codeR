package fr.rob.game.test.unit.domain.game.world.packet

import com.google.protobuf.ByteString
import com.google.protobuf.Empty
import com.google.protobuf.Message
import fr.raven.log.NullLogger
import fr.rob.game.entity.guid.ObjectGuidGenerator
import fr.rob.game.player.Player
import fr.rob.game.player.session.GameSession
import fr.rob.game.player.session.SessionState
import fr.rob.game.test.unit.sandbox.network.session.NullMessageSender
import fr.rob.game.world.function.WorldFunctionInterface
import fr.rob.game.world.function.WorldFunctionRegistry.WorldFunctionItem
import fr.rob.game.world.function.WorldFunctionRegistry
import fr.rob.game.world.packet.WorldPacket
import fr.rob.game.world.packet.WorldPacketQueue
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

class WorldPacketQueueTest {
    private class RecordingFunction(private val throwing: Boolean = false) : WorldFunctionInterface {
        var calls = 0

        override fun invoke(sender: GameSession, opcode: Int, message: Message) {
            calls++
            if (throwing) throw IllegalStateException("boom")
        }

        override fun parseFromByteString(data: ByteString): Message = Empty.getDefaultInstance()
    }

    private fun session(accountId: Int = 1) = GameSession(accountId, NullMessageSender())

    private fun packet(session: GameSession, opcode: Int) = WorldPacket(session, opcode, Empty.getDefaultInstance())

    private fun queueWith(vararg items: WorldFunctionItem) =
        WorldPacketQueue(WorldFunctionRegistry(arrayOf(*items)), NullLogger())

    private fun sessionInState(state: SessionState): GameSession {
        val session = session()

        if (state == SessionState.AUTHENTICATED) {
            return session
        }

        session.beginLoading()

        if (state == SessionState.IN_WORLD) {
            session.enterWorld(Player(session, ObjectGuidGenerator().createForPlayer(1), "Test", 1))
        }

        return session
    }

    @Test
    fun `unknown opcode is dropped and counted as rejected`() {
        val ok = RecordingFunction()
        val queue = queueWith(WorldFunctionItem(1, ok, SessionState.AUTHENTICATED))
        val session = session()

        queue.enqueue(packet(session, 999))
        queue.enqueue(packet(session, 1))
        queue.dequeue()

        assertEquals(1, ok.calls)
        assertEquals(1, session.rejectedPacketCount)
    }

    @ParameterizedTest
    @EnumSource(SessionState::class)
    fun `handler only runs when the session is in the required state`(sessionState: SessionState) {
        val handlers = SessionState.entries.associateWith { RecordingFunction() }
        val queue = queueWith(
            *SessionState.entries.mapIndexed { opcode, state -> WorldFunctionItem(opcode, handlers.getValue(state), state) }
                .toTypedArray(),
        )
        val session = sessionInState(sessionState)

        SessionState.entries.indices.forEach { queue.enqueue(packet(session, it)) }
        queue.dequeue()

        handlers.forEach { (state, handler) -> assertEquals(if (state == sessionState) 1 else 0, handler.calls, "$state") }
        assertEquals(SessionState.entries.size - 1, session.rejectedPacketCount)
    }

    @Test
    fun `a second login while loading is rejected`() {
        val login = object : WorldFunctionInterface {
            var calls = 0

            override fun invoke(sender: GameSession, opcode: Int, message: Message) {
                calls++
                sender.beginLoading()
            }

            override fun parseFromByteString(data: ByteString): Message = Empty.getDefaultInstance()
        }
        val queue = queueWith(WorldFunctionItem(1, login, SessionState.AUTHENTICATED))
        val session = session()

        queue.enqueue(packet(session, 1))
        queue.enqueue(packet(session, 1))
        queue.dequeue()

        assertEquals(1, login.calls)
        assertEquals(SessionState.LOADING, session.state)
        assertEquals(1, session.rejectedPacketCount)
    }

    @Test
    fun `failing handler does not prevent next packets`() {
        val failing = RecordingFunction(throwing = true)
        val ok = RecordingFunction()
        val queue = queueWith(
            WorldFunctionItem(1, failing, SessionState.AUTHENTICATED),
            WorldFunctionItem(2, ok, SessionState.AUTHENTICATED),
        )

        queue.enqueue(packet(session(1), 1))
        queue.enqueue(packet(session(1), 2))
        queue.enqueue(packet(session(2), 2))
        queue.dequeue()

        assertEquals(1, failing.calls)
        assertEquals(2, ok.calls)
    }

    @Test
    fun `drained per-session queues are released`() {
        val queue = queueWith(WorldFunctionItem(1, RecordingFunction(), SessionState.AUTHENTICATED))

        (1..50).forEach { queue.enqueue(packet(session(it), 1)) }
        assertEquals(50, queue.size())

        queue.dequeue()
        assertEquals(0, queue.size())
    }

    @Test
    fun `removeQueue drops a pending session queue`() {
        val queue = queueWith()
        val session = session(7)

        queue.enqueue(packet(session, 1))
        queue.removeQueue(session)

        assertEquals(0, queue.size())
    }

    @Test
    fun `late close of the old gateway does not drop the queue of the reconnected session`() {
        val ok = RecordingFunction()
        val queue = queueWith(WorldFunctionItem(1, ok, SessionState.AUTHENTICATED))
        val staleSessionOnOldGateway = session(7)
        val reconnectedSessionOnNewGateway = session(7)

        queue.enqueue(packet(staleSessionOnOldGateway, 1))
        queue.enqueue(packet(reconnectedSessionOnNewGateway, 1))
        assertEquals(2, queue.size())

        queue.removeQueue(staleSessionOnOldGateway)
        queue.dequeue()

        assertEquals(1, ok.calls)
    }
}
