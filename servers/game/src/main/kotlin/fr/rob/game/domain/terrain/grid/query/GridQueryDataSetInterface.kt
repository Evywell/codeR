package fr.rob.game.domain.terrain.grid.query

import fr.rob.game.domain.entity.WorldObject
import fr.rob.game.domain.terrain.grid.Grid

interface GridQueryDataSetInterface {
    fun getObjects(grid: Grid): List<WorldObject>
}
