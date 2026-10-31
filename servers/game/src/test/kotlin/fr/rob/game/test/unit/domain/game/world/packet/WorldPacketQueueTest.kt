package fr.rob.game.test.unit.domain.game.world.packet

import com.google.protobuf.ByteString
import com.google.protobuf.Empty
import com.google.protobuf.Message
import fr.raven.log.NullLogger
import fr.rob.game.player.session.GameSession
import fr.rob.game.test.unit.sandbox.network.session.NullMessageSender
import fr.rob.game.world.function.WorldFunctionInterface
import fr.rob.game.world.function.WorldFunctionRegistry
import fr.rob.game.world.packet.WorldPacket
import fr.rob.game.world.packet.WorldPacketQueue
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class WorldPacketQueueTest {
    private class RecordingFunction(private val throwing: Boolean = false) : WorldFunctionInterface {
        var calls = 0

        override fun invoke(sender: GameSession, opcode: Int, message: Message) {
            calls++
            if (throwing) throw IllegalStateException("boom")
        }

        override fun parseFromByteString(data: ByteString): Message = Empty.getDefaultInstance()
    }

    private fun packet(accountId: Int, opcode: Int) =
        WorldPacket(GameSession(accountId, NullMessageSender()), opcode, Empty.getDefaultInstance())

    private fun queueWith(vararg items: WorldFunctionRegistry.WorldFunctionItem) =
        WorldPacketQueue(WorldFunctionRegistry(arrayOf(*items)), NullLogger())

    @Test
    fun `unknown opcode is dropped without throwing`() {
        val ok = RecordingFunction()
        val queue = queueWith(WorldFunctionRegistry.WorldFunctionItem(1, ok))

        queue.enqueue(packet(1, 999))
        queue.enqueue(packet(1, 1))
        queue.dequeue()

        assertEquals(1, ok.calls)
    }

    @Test
    fun `failing handler does not prevent next packets`() {
        val failing = RecordingFunction(throwing = true)
        val ok = RecordingFunction()
        val queue = queueWith(
            WorldFunctionRegistry.WorldFunctionItem(1, failing),
            WorldFunctionRegistry.WorldFunctionItem(2, ok),
        )

        queue.enqueue(packet(1, 1))
        queue.enqueue(packet(1, 2))
        queue.enqueue(packet(2, 2))
        queue.dequeue()

        assertEquals(1, failing.calls)
        assertEquals(2, ok.calls)
    }

    @Test
    fun `drained per-account queues are released`() {
        val queue = queueWith(WorldFunctionRegistry.WorldFunctionItem(1, RecordingFunction()))

        (1..50).forEach { queue.enqueue(packet(it, 1)) }
        assertEquals(50, queue.size())

        queue.dequeue()
        assertEquals(0, queue.size())
    }

    @Test
    fun `removeQueue drops a pending session queue`() {
        val queue = queueWith()
        val session = GameSession(7, NullMessageSender())

        queue.enqueue(WorldPacket(session, 1, Empty.getDefaultInstance()))
        queue.removeQueue(session)

        assertEquals(0, queue.size())
    }

    @Test
    fun `late close of the old gateway does not drop the queue of the reconnected session`() {
        val ok = RecordingFunction()
        val queue = queueWith(WorldFunctionRegistry.WorldFunctionItem(1, ok))
        val staleSessionOnOldGateway = GameSession(7, NullMessageSender())
        val reconnectedSessionOnNewGateway = GameSession(7, NullMessageSender())

        queue.enqueue(WorldPacket(staleSessionOnOldGateway, 1, Empty.getDefaultInstance()))
        queue.enqueue(WorldPacket(reconnectedSessionOnNewGateway, 1, Empty.getDefaultInstance()))
        assertEquals(2, queue.size())

        queue.removeQueue(staleSessionOnOldGateway)
        queue.dequeue()

        assertEquals(1, ok.calls)
    }
}
