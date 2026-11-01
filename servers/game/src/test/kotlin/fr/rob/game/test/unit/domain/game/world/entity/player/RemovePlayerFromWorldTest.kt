package fr.rob.game.test.unit.domain.game.world.entity.player

import fr.raven.log.NullLogger
import fr.rob.core.network.v2.session.SessionSocketInterface
import fr.rob.game.entity.Position
import fr.rob.game.entity.guid.ObjectGuidGenerator
import fr.rob.game.network.GameNodeServer
import fr.rob.game.network.packet.BytesToMessageBuilder
import fr.rob.game.network.session.GatewayGameSession
import fr.rob.game.player.Player
import fr.rob.game.player.action.DisconnectedSessionProcessor
import fr.rob.game.player.action.RemovePlayerFromWorldHandler
import fr.rob.game.player.session.DisconnectedSessionQueue
import fr.rob.game.player.session.GameSession
import fr.rob.game.player.session.SessionState
import fr.rob.game.test.unit.sandbox.network.session.NullMessageSender
import fr.rob.game.test.unit.tools.WorldBuilder
import fr.rob.game.world.function.WorldFunctionRegistry
import fr.rob.game.world.packet.WorldPacketQueue
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class RemovePlayerFromWorldTest {
    private val worldBuilder = WorldBuilder()
    private val instance = worldBuilder.buildBasicWorld()
    private val handler = RemovePlayerFromWorldHandler()

    private fun playerInWorld(session: GameSession): Player {
        val player = Player(session, ObjectGuidGenerator().createForPlayer(session.accountId), "Test", 1)
        session.beginLoading()
        worldBuilder.addIntoInstance(player, instance, Position(0f, 0f, 0f, 0f))
        session.enterWorld(player)

        return player
    }

    @Test
    fun `removing a player clears the link on both sides and schedules the removal`() {
        val session = GameSession(1, NullMessageSender())
        val player = playerInWorld(session)

        handler.execute(session)

        assertEquals(SessionState.AUTHENTICATED, session.state)
        assertNull(session.controlledWorldObject)
        assertNull(player.controlledByGameSession)
        assertFalse(player.isInWorld)
        assertEquals(listOf(player), instance.consumeScheduledRemovals())
    }

    @Test
    fun `removing a session that is not in the world does nothing`() {
        val session = GameSession(1, NullMessageSender())

        handler.execute(session)

        assertEquals(SessionState.AUTHENTICATED, session.state)
    }

    @Test
    fun `closing the gateway connection removes its players on the world thread`() {
        val disconnectedSessions = DisconnectedSessionQueue()
        val processor = DisconnectedSessionProcessor(disconnectedSessions, handler)
        val server = GameNodeServer(
            WorldPacketQueue(WorldFunctionRegistry(arrayOf()), NullLogger()),
            BytesToMessageBuilder(WorldFunctionRegistry(arrayOf())),
            NullLogger(),
            disconnectedSessions,
        )
        val gateway = server.createSession(
            object : SessionSocketInterface {
                override fun getIp(): String = "127.0.0.1"
                override fun send(data: Any) {}
                override fun close() {}
            },
        ) as GatewayGameSession
        val inWorld = gateway.findOrCreateGameSession(1)
        val authenticated = gateway.findOrCreateGameSession(2)
        val player = playerInWorld(inWorld)

        server.onConnectionClosed(gateway)

        assertTrue(gateway.getGameSessions().isEmpty())
        assertTrue(player.isInWorld, "world state must not change on the network thread")

        processor.update(0)

        assertFalse(player.isInWorld)
        assertTrue(inWorld.isClosed)
        assertTrue(authenticated.isClosed)
        assertEquals(SessionState.AUTHENTICATED, inWorld.state)
    }
}
