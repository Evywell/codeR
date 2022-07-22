package fr.rob.game.test.unit.domain.game.world.grid

import fr.rob.game.domain.terrain.grid.Cell
import fr.rob.game.domain.terrain.grid.GridBuilder
import fr.rob.game.domain.terrain.grid.GridConstraintChecker
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GridTest {

    @ParameterizedTest
    @MethodSource("neighborCellDataProvider")
    fun `I must give the neighbor cells`(baseCellIndex: Int, neighbor: Array<Cell>) {
        // Arrange
        val builder = GridBuilder(GridConstraintChecker())
        val grid = builder.buildGrid(10, 100, 100)

        // Act
        val cells = grid.retrieveNeighborCells(grid.cells[baseCellIndex])

        // Assert
        Assertions.assertEquals(neighbor.size, cells.size)

        for (i in neighbor.indices) {
            Assertions.assertEquals(neighbor[i].x, cells[i].x)
            Assertions.assertEquals(neighbor[i].y, cells[i].y)
        }
    }

    fun neighborCellDataProvider(): Stream<Arguments> = Stream.of(
        Arguments.of(
            23,
            arrayOf(
                Cell(2, 1),
                Cell(2, 2),
                Cell(2, 3),
                Cell(3, 1),
                Cell(3, 2),
                Cell(3, 3),
                Cell(4, 1),
                Cell(4, 2),
                Cell(4, 3),
            )
        ),
        Arguments.of(
            99,
            arrayOf(
                Cell(8, 8),
                Cell(8, 9),
                Cell(9, 8),
                Cell(9, 9),
            ),
        ),
        Arguments.of(
            0,
            arrayOf(
                Cell(0, 0),
                Cell(0, 1),
                Cell(1, 0),
                Cell(1, 1),
            ),
        ),
        Arguments.of(
            60,
            arrayOf(
                Cell(0, 5),
                Cell(0, 6),
                Cell(0, 7),
                Cell(1, 5),
                Cell(1, 6),
                Cell(1, 7),
            ),
        ),
    )
}
