package fr.rob.game.domain.entity

import fr.rob.game.domain.terrain.grid.Cell
import kotlin.math.ceil

class PositionNormalizer {

    fun fromMapPositionToGridCellCoordinate(mapInfo: MapInfoForPosition): Cell.CellPosition {
        val normalizedX = (mapInfo.position.x + mapInfo.mapZoneWidth / 2) - mapInfo.mapZoneOffsetX
        val normalizedY = (mapInfo.position.y + mapInfo.mapZoneHeight / 2) - mapInfo.mapZoneOffsetY

        val cellX = ceil(normalizedX / mapInfo.gridCellSize) - 1
        val cellY = ceil(normalizedY / mapInfo.gridCellSize) - 1

        return Cell.CellPosition(cellX.toInt(), cellY.toInt())
    }

    data class MapInfoForPosition(
        val position: Position,
        val mapZoneWidth: Int,
        val mapZoneHeight: Int,
        val mapZoneOffsetX: Float,
        val mapZoneOffsetY: Float,
        val gridCellSize: Int,
    )
}
