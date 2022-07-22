package fr.rob.game.domain.terrain.grid

import kotlin.math.floor

class GridBuilder(private val constraintChecker: GridConstraintChecker) {

    fun buildGrid(cellSize: Int, zoneWidth: Int, zoneHeight: Int): Grid {
        constraintChecker.checkGridConfiguration(cellSize, zoneWidth, zoneHeight)

        val gridWith = (zoneWidth / cellSize)
        val gridHeight = (zoneHeight / cellSize)
        val cellsNb = gridWith * gridHeight
        val cells = Array(cellsNb) {
            val x = it % gridHeight
            val y = floor(it / gridWith.toDouble()).toInt()

            Cell(x, y)
        }

        return Grid(gridWith, gridHeight, cellSize, cells)
    }

    companion object {
        const val DEFAULT_CELL_SIZE = 10
        const val NEIGHBOR_RADIUS_CELLS = 1
    }
}
