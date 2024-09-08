package fr.rob.game.domain.entity.notifier

import fr.rob.game.app.player.message.MovementHeartbeatMessage
import fr.rob.game.domain.entity.WorldObject
import fr.rob.game.domain.entity.movement.Movable
import fr.rob.game.domain.player.Player

class WorldObjectUpdatedNotifier : WorldObjectVisitorInterface {
    override fun visit(gameObject: WorldObject) {
        val grid = gameObject.mapInstance.grid
        val cells = grid.retrieveNeighborCells(gameObject.cell!!)
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
