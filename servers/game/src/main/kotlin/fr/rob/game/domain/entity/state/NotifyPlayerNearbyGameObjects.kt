package fr.rob.game.domain.entity.state

import fr.rob.game.app.state.ActionHandlerInterface
import fr.rob.game.domain.entity.Position
import fr.rob.game.domain.entity.WorldObject
import fr.rob.game.domain.entity.guid.ObjectGuid
import fr.rob.game.domain.player.Player
import fr.rob.game.domain.player.session.GameMessageHolder
import fr.rob.game.domain.player.session.GameMessageInterface
import fr.rob.game.domain.player.session.GameSession

class NotifyPlayerNearbyGameObjects : ActionHandlerInterface<ObjectAddedToGrid> {
    override fun invoke(action: ObjectAddedToGrid) {
        val worldObject = action.worldObject

        if (!worldObject.guid.isPlayer()) {
            return
        }

        assert(worldObject.cell != null)
        val grid = worldObject.mapInstance.grid
        val cells = grid.retrieveNeighborCells(worldObject.cell!!)
        val worldObjectToBeNotifiedOf = ArrayList<WorldObject>()

        for (cell in cells) {
            worldObjectToBeNotifiedOf.addAll(grid.getObjectsOfCell(cell))
        }

        // Notify the player
        worldObject as Player
        // Notify worldObject.ownerGameSession
        if (worldObject.controlledByGameSession != null) {
            // Notify worldObject.controlledByGameSession
            worldObjectToBeNotifiedOf.forEach { obj -> notifyForObject(worldObject.controlledByGameSession!!, obj) }
        }
    }

    private fun notifyForObject(session: GameSession, obj: WorldObject) {
        if (obj.controlledByGameSession != null && obj.controlledByGameSession == session) {
            return
        }

        session.send(NearbyObjectMessage(obj.guid, obj.position))
    }

    override fun getType(): String = ObjectAddedToGrid::class.qualifiedName!!

    data class NearbyObjectMessage(
        val objectId: ObjectGuid,
        val position: Position
    ) : GameMessageInterface {
        override fun createGameMessageHolder(): GameMessageHolder = GameMessageHolder(0x03, this)
    }
}
