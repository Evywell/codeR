package fr.rob.game.test.unit.domain.game.world.entity.player

import fr.rob.game.entity.guid.ObjectGuidGenerator
import fr.rob.game.player.Player
import fr.rob.game.player.session.GameSession
import fr.rob.game.player.session.IllegalSessionStateTransitionException
import fr.rob.game.player.session.SessionState
import fr.rob.game.test.unit.sandbox.network.session.NullMessageSender
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GameSessionTest {
    private val session = GameSession(1, NullMessageSender())
    private val player = Player(session, ObjectGuidGenerator().createForPlayer(1), "Test", 1)

    @Test
    fun `a new session is authenticated without controlled object`() {
        assertEquals(SessionState.AUTHENTICATED, session.state)
        assertNull(session.controlledWorldObject)
    }

    @Test
    fun `entering the world sets the controlled object`() {
        session.beginLoading()
        session.enterWorld(player)

        assertEquals(SessionState.IN_WORLD, session.state)
        assertSame(player, session.controlledWorldObject)
        assertSame(player, session.loggedAsPlayer)
    }

    @Test
    fun `leaving the world clears the controlled object`() {
        session.beginLoading()
        session.enterWorld(player)
        session.leaveWorld()

        assertEquals(SessionState.AUTHENTICATED, session.state)
        assertNull(session.controlledWorldObject)
        assertNull(session.loggedAsPlayer)
    }

    @Test
    fun `cancelled loading goes back to authenticated`() {
        session.beginLoading()
        session.cancelLoading()

        assertEquals(SessionState.AUTHENTICATED, session.state)
    }

    @Test
    fun `entering the world without loading is refused`() {
        assertThrows<IllegalSessionStateTransitionException> { session.enterWorld(player) }
        assertEquals(SessionState.AUTHENTICATED, session.state)
    }

    @Test
    fun `loading twice is refused`() {
        session.beginLoading()

        assertThrows<IllegalSessionStateTransitionException> { session.beginLoading() }
    }

    @Test
    fun `leaving the world when not in it is refused`() {
        assertThrows<IllegalSessionStateTransitionException> { session.leaveWorld() }
    }
}
