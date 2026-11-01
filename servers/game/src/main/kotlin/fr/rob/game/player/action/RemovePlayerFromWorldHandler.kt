package fr.rob.game.player.action

import fr.rob.game.player.session.GameSession
import fr.rob.game.player.session.SessionState

/**
 * Single path to take a player out of the world: on request (CMSG_REMOVE_FROM_WORLD) or when its connection is lost.
 * Must run on the world thread.
 */
class RemovePlayerFromWorldHandler {
    fun execute(session: GameSession) {
        if (session.state != SessionState.IN_WORLD) {
            return
        }

        session.controlledWorldObject?.scheduleRemoveFromInstance()
        session.leaveWorld()
    }
}
