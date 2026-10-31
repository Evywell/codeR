package fr.rob.game.test.unit.domain.game.world.grid

import fr.rob.game.entity.Position
import fr.rob.game.map.grid.GridBuilder
import fr.rob.game.map.grid.GridConstraintChecker
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class GridContainsTest {
    // 100x100 world units centered on 0: valid x and y are in (-50, 50]
    private val grid = GridBuilder(GridConstraintChecker()).buildGrid(10, 100, 100)

    @ParameterizedTest
    @ValueSource(floats = [Float.NaN, Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY])
    fun `non-finite coordinates are outside`(value: Float) {
        assertFalse(grid.contains(Position(value, 0f, 0f, 0f)))
        assertFalse(grid.contains(Position(0f, value, 0f, 0f)))
        assertFalse(grid.contains(Position(0f, 0f, value, 0f)))
    }

    @Test
    fun `positions beyond the edges are outside`() {
        assertFalse(grid.contains(Position(50.1f, 0f, 0f, 0f)))
        assertFalse(grid.contains(Position(-50f, 0f, 0f, 0f)))
        assertFalse(grid.contains(Position(0f, 1_000_000f, 0f, 0f)))
    }

    @Test
    fun `positions inside are contained and resolve to a cell`() {
        listOf(Position(0f, 0f, 0f, 0f), Position(50f, 50f, 0f, 0f), Position(-49.9f, -49.9f, 0f, 0f)).forEach {
            assertTrue(grid.contains(it))
            grid.getCellFromWorldPosition(it)
        }
    }
}
