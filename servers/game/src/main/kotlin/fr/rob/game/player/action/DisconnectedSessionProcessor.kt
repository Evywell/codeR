package fr.rob.game.player.action

import fr.rob.game.player.session.DisconnectedSessionQueue
import fr.rob.game.world.UpdatableInterface

/**
 * Takes the players of disconnected sessions out of the world. Runs on the world thread.
 */
class DisconnectedSessionProcessor(
    private val disconnectedSessionQueue: DisconnectedSessionQueue,
    private val removePlayerFromWorldHandler: RemovePlayerFromWorldHandler,
) : UpdatableInterface {
    override fun update(deltaTime: Int) {
        while (true) {
            val session = disconnectedSessionQueue.poll() ?: return

            session.close()
            removePlayerFromWorldHandler.execute(session)
        }
    }
}
