package fr.rob.game.domain.terrain.grid.query

import fr.rob.game.domain.entity.WorldObject
import fr.rob.game.domain.terrain.grid.Cell
import fr.rob.game.domain.terrain.grid.Grid

class GridQuery(private val grid: Grid) {
    fun getObjects(cell: Cell, predicates: Array<GridQueryPredicateInterface>): List<WorldObject> {
        val allCells = grid.retrieveNeighborCells(cell)
        val objects = ArrayList<WorldObject>()

        grid.iterateOverObjects { worldObject ->
            if (allCells.contains(worldObject.cell)) {
                var isWorldObjectMatchingPredicate = true

                for (predicate in predicates) {
                    if (!predicate.resolve(worldObject)) {
                        isWorldObjectMatchingPredicate = false
                        break
                    }
                }

                if (isWorldObjectMatchingPredicate) {
                    objects.add(worldObject)
                }
            }
        }

        return objects
    }
}
