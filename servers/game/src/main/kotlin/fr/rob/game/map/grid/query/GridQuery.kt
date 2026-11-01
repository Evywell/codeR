package fr.rob.game.map.grid.query

import fr.rob.game.entity.WorldObject
import fr.rob.game.map.grid.Cell
import fr.rob.game.map.grid.Grid

class GridQuery(private val grid: Grid) {
    fun getObjects(cell: Cell, predicates: Array<GridQueryPredicateInterface>): List<WorldObject> {
        return grid.getObjectsOfCells(grid.retrieveNeighborCells(cell))
            .filter { worldObject -> predicates.all { it.resolve(worldObject) } }
    }
}
