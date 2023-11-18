package fr.rob.game.test.unit.domain.game.world

import fr.rob.game.domain.entity.Position
import fr.rob.game.domain.entity.PositionNormalizer
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class PositionNormalizerTest {
    @Test
    fun `Convert map position to grid cell coordinates`() {
        // Arrange
        val positionA = Position(-220f, -100f, 0f, 0f)
        val positionB = Position(90f, 40f, 0f, 0f)

        // Act
        val cellCoordinatesA = PositionNormalizer.fromMapPositionToGridCellCoordinate(
            PositionNormalizer.MapInfoForPosition(
                positionA,
                500,
                500,
                -50f,
                -50f,
                100,
            ),
        )

        val cellCoordinatesB = PositionNormalizer.fromMapPositionToGridCellCoordinate(
            PositionNormalizer.MapInfoForPosition(
                positionB,
                500,
                500,
                -50f,
                -50f,
                100,
            ),
        )

        // Assert
        assertEquals(0, cellCoordinatesA.x)
        assertEquals(1, cellCoordinatesA.y)

        assertEquals(3, cellCoordinatesB.x)
        assertEquals(3, cellCoordinatesB.y)
    }
}
