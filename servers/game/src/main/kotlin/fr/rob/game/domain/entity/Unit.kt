package fr.rob.game.domain.entity

import fr.rob.game.app.player.message.NearbyObjectMessage
import fr.rob.game.domain.entity.behavior.MovingBehavior
import fr.rob.game.domain.entity.guid.ObjectGuid
import fr.rob.game.domain.entity.movement.WorldObjectMovedEvent
import fr.rob.game.domain.player.Player

open class Unit(
    guid: ObjectGuid,
    val name: String,
    var level: Int
) : WorldObject(guid) {
    var currentMovement: Movement? = null
        private set

    fun addToWorld(objectManager: ObjectManager) {
        objectManager.addToGrid(this)
        this.isInWorld = true
    }

    fun setMoving(behavior: MovingBehavior, movement: Movement) {
        currentMovement = movement
        behaviors.add(behavior)
    }

    fun isMoving(): Boolean = currentMovement != null

    fun moveTo(x: Float, y: Float, z: Float, orientation: Float) {
        position.x = x
        position.y = y
        position.z = z
        position.orientation = orientation

        pushEvent(WorldObjectMovedEvent(this))
        // notifyNearbyPlayers()
    }

    private fun notifyNearbyPlayers() {
        val grid = mapInstance.grid
        val cells = grid.retrieveNeighborCells(cell!!)
        val playersToNotify = ArrayList<Player>()

        for (cell in cells) {
            playersToNotify.addAll(grid.getPlayersOfCell(cell))
        }

        val positionUpdatedMessage = NearbyObjectMessage(guid, position)

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
