package fr.rob.game.domain.entity

import fr.rob.game.domain.instance.MapInstance
import fr.rob.game.domain.terrain.grid.Cell
import kotlin.math.ceil

class PositionNormalizer {

    /**
     * @todo CHANGE MAP INSTANCE INTO TO PARAMS: MAP AND GRID
     */
    fun fromMapPositionToGridCellCoordinate(position: Position, mapInstance: MapInstance): Cell.CellPosition {
        val map = mapInstance.map
        val grid = mapInstance.grid

        val normalizedX = (position.x + map.zoneInfo.width / 2) - map.zoneInfo.offsetX
        val normalizedY = (position.y + map.zoneInfo.height / 2) - map.zoneInfo.offsetY

        val cellX = ceil(normalizedX / grid.cellSize) - 1
        val cellY = ceil(normalizedY / grid.cellSize) - 1

        return Cell.CellPosition(cellX.toInt(), cellY.toInt())
    }
}
