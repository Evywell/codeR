package fr.rob.game.test.unit.domain.game.world.grid

import fr.rob.game.entity.Position
import fr.rob.game.map.grid.Cell
import fr.rob.game.map.grid.GridBuilder
import fr.rob.game.map.grid.GridConstraintChecker
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class GridNeighborCellsTest {
    // 10 cells wide, 4 cells high
    private val grid = GridBuilder(GridConstraintChecker()).buildGrid(10, 100, 40)

    @Test
    fun `cells are laid out row by row on a non-square grid`() {
        assertEquals(40, grid.cells.size)
        grid.cells.forEachIndexed { index, cell -> assertEquals(Cell(index % 10, index / 10), cell) }
    }

    @Test
    fun `world positions map to the right cell on a non-square grid`() {
        // x in (-50, 50], y in (-20, 20]
        assertEquals(Cell(0, 0), grid.getCellFromWorldPosition(Position(-49f, -19f, 0f, 0f)))
        assertEquals(Cell(9, 0), grid.getCellFromWorldPosition(Position(49f, -19f, 0f, 0f)))
        assertEquals(Cell(9, 3), grid.getCellFromWorldPosition(Position(49f, 19f, 0f, 0f)))
        assertEquals(Cell(0, 3), grid.getCellFromWorldPosition(Position(-49f, 19f, 0f, 0f)))
    }

    @Test
    fun `neighbors with radius 2 are centered on the cell`() {
        val cells = grid.retrieveNeighborCells(Cell(5, 2), 2).toSet()

        val expected = (3..7).flatMap { x -> (0..3).map { y -> Cell(x, y) } }.toSet()
        assertEquals(expected, cells)
    }

    @Test
    fun `neighbors are clipped at the grid corner`() {
        val cells = grid.retrieveNeighborCells(Cell(9, 3), 2).toSet()

        val expected = (7..9).flatMap { x -> (1..3).map { y -> Cell(x, y) } }.toSet()
        assertEquals(expected, cells)
    }

    @Test
    fun `radius 0 returns only the cell`() {
        assertEquals(listOf(Cell(4, 1)), grid.retrieveNeighborCells(Cell(4, 1), 0).toList())
    }
}
