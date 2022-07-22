package fr.rob.game.domain.terrain.grid

import fr.rob.game.domain.terrain.grid.exception.NegativeOrNullCellSizeException
import fr.rob.game.domain.terrain.grid.exception.ZoneDimensionInvalidException

class GridConstraintChecker {
    fun checkGridConfiguration(cellSize: Int, zoneWidth: Int, zoneHeight: Int) {
        if (cellSize <= 0) {
            throw NegativeOrNullCellSizeException()
        }

        if (isIncorrectZoneWidth(cellSize, zoneWidth)) {
            throw ZoneDimensionInvalidException(cellSize, "width")
        }

        if (isIncorrectZoneHeight(cellSize, zoneHeight)) {
            throw ZoneDimensionInvalidException(cellSize, "height")
        }
    }

    private fun isIncorrectZoneWidth(cellSize: Int, zoneWidth: Int): Boolean =
        isIncorrectZoneDimension(cellSize, zoneWidth)

    private fun isIncorrectZoneHeight(cellSize: Int, zoneHeight: Int): Boolean =
        isIncorrectZoneDimension(cellSize, zoneHeight)

    private fun isIncorrectZoneDimension(cellSize: Int, zoneDimension: Int): Boolean =
        zoneDimension % cellSize > 0
}
