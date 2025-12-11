package fr.rob.game.map.grid.query

import fr.rob.game.entity.WorldObject
import fr.rob.game.map.grid.Grid

interface GridQueryDataSetInterface {
    fun getObjects(grid: Grid): List<WorldObject>
}
