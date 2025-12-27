package fr.rob.game.entity.notifier

import fr.rob.game.player.message.MovementHeartbeatMessage
import fr.rob.game.entity.WorldObject
import fr.rob.game.entity.movement.Movable
import fr.rob.game.player.Player

class WorldObjectUpdatedNotifier : WorldObjectVisitorInterface {
    override fun visit(gameObject: WorldObject) {
        val grid = gameObject.mapInstance.grid
        val cells = grid.retrieveNeighborCells(gameObject.getCell())
        val playersToNotify = ArrayList<Player>()

        for (cell in cells) {
            playersToNotify.addAll(grid.getPlayersOfCell(cell))
        }

        var movement: Movable.Movement? = null

        gameObject.getTrait<Movable>().ifPresent { movement = it.currentMovement }
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
