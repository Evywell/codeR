package fr.rob.game.test.unit.domain.game.world

import fr.rob.game.domain.entity.Position
import fr.rob.game.domain.entity.PositionNormalizer
import fr.rob.game.domain.terrain.grid.GridBuilder
import fr.rob.game.domain.terrain.grid.GridConstraintChecker
import fr.rob.game.domain.terrain.map.Map
import fr.rob.game.domain.terrain.map.MapInfo
import fr.rob.game.domain.terrain.map.ZoneInfo
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class PositionNormalizerTest {
    @Test
    fun `Convert map position to grid cell coordinates`() {
        // Arrange
        val normalizer = PositionNormalizer()
        val gridBuilder = GridBuilder(GridConstraintChecker())
        val mapInfo = MapInfo("A testing map", 2000, 2000)
        val zoneInfo = ZoneInfo("A testing zone", 500, 500, -50f, -50f)
        val map = Map(1, 1, mapInfo, zoneInfo)
        val grid = gridBuilder.buildGrid(100, zoneInfo.width, zoneInfo.height)

        val positionA = Position(-220f, -100f, 0f, 0f)
        val positionB = Position(90f, 40f, 0f, 0f)

        // Act
        val cellCoordinatesA = normalizer.fromMapPositionToGridCellCoordinate(
            PositionNormalizer.MapInfoForPosition(
                positionA,
                500,
                500,
                -50f,
                -50f,
                100
            )
        )

        val cellCoordinatesB = normalizer.fromMapPositionToGridCellCoordinate(
            PositionNormalizer.MapInfoForPosition(
                positionB,
                500,
                500,
                -50f,
                -50f,
                100
            )
        )

        // Assert
        assertEquals(0, cellCoordinatesA.x)
        assertEquals(1, cellCoordinatesA.y)

        assertEquals(3, cellCoordinatesB.x)
        assertEquals(3, cellCoordinatesB.y)
    }
}
