package fr.rob.game.domain.terrain.grid.query

import fr.rob.game.domain.entity.WorldObject
import fr.rob.game.domain.entity.guid.ObjectGuid
import fr.rob.game.domain.terrain.grid.Grid

class OnPlayers : GridQueryDataSetInterface {
    override fun getObjects(grid: Grid): List<WorldObject> = grid.getObjectsByType(ObjectGuid.GUID_TYPE.PLAYER)
}
