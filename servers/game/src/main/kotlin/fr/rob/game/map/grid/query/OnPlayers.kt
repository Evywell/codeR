package fr.rob.game.map.grid.query

import fr.rob.game.entity.WorldObject
import fr.rob.game.entity.guid.ObjectGuid
import fr.rob.game.map.grid.Grid

class OnPlayers : GridQueryDataSetInterface {
    override fun getObjects(grid: Grid): List<WorldObject> = grid.getObjectsByType(ObjectGuid.GUID_TYPE.PLAYER)
}
