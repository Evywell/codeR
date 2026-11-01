package fr.rob.game.entity.notifier

import fr.rob.game.component.MovementComponent
import fr.rob.game.entity.WorldObject
import fr.rob.game.entity.visibility.VisibilityService
import fr.rob.game.player.Player
import fr.rob.game.player.message.MovementHeartbeatMessage

/**
 * Sends the object position to the players that see it, and to the object itself when it is a player.
 */
class WorldObjectUpdatedNotifier(
    private val visibilityService: VisibilityService,
) : WorldObjectVisitorInterface {
    override fun visit(gameObject: WorldObject) {
        val playersToNotify = visibilityService.observersOf(gameObject).toMutableList()

        if (gameObject is Player) {
            playersToNotify.add(gameObject)
        }

        val movement = gameObject.getComponent<MovementComponent>()?.movementInfo

        val positionUpdatedMessage = MovementHeartbeatMessage(gameObject.guid, gameObject.position, movement)

        playersToNotify.forEach { player ->
            val ownerGameSession = player.ownerGameSession
            ownerGameSession.send(positionUpdatedMessage)

            val controlledByGameSession = player.controlledByGameSession

            if (controlledByGameSession != null && controlledByGameSession != ownerGameSession) {
                controlledByGameSession.send(positionUpdatedMessage)
            }
        }
    }
}
