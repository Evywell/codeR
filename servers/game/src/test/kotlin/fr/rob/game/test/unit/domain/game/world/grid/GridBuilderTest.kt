package fr.rob.game.test.unit.domain.game.world.grid

import fr.rob.game.map.grid.GridBuilder
import fr.rob.game.map.grid.GridConstraintChecker
import fr.rob.game.map.grid.exception.NegativeOrNullCellSizeException
import fr.rob.game.map.grid.exception.ZoneDimensionInvalidException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GridBuilderTest {

    @Test
    fun `When providing a positive cell size, the grid should be created`() {
        // Arrange, Act & Assert
        GridBuilder(GridConstraintChecker()).buildGrid(5, 100, 100)
    }

    @Test
    fun `When providing a negative or null cell size, an exception must be thrown`() {
        // Arrange, Act & Assert
        assertThrows(NegativeOrNullCellSizeException::class.java) {
            GridBuilder(GridConstraintChecker()).buildGrid(-1, 100, 100)
        }

        assertThrows(NegativeOrNullCellSizeException::class.java) {
            GridBuilder(GridConstraintChecker()).buildGrid(0, 100, 100)
        }
    }

    @Test
    fun `When providing a width not divisible by the cell size, an exception must be thrown`() {
        // Arrange, Act & Assert
        assertThrows(ZoneDimensionInvalidException::class.java) {
            GridBuilder(GridConstraintChecker()).buildGrid(3, 100, 30)
        }
    }

    @Test
    fun `When providing an height not divisible by the cell size, an exception must be thrown`() {
        // Arrange, Act & Assert
        assertThrows(ZoneDimensionInvalidException::class.java) {
            GridBuilder(GridConstraintChecker()).buildGrid(3, 30, 100)
        }
    }

    @ParameterizedTest
    @MethodSource("validGridsDataProvider")
    fun `When building a grid, I should have the right number of cells`(
        cellSize: Int,
        width: Int,
        height: Int,
        expected: Int
    ) {
        // Arrange
        val builder = GridBuilder(GridConstraintChecker())

        // Act
        val grid = builder.buildGrid(cellSize, width, height)

        // Assert
        assertEquals(expected, grid.cells.size)
    }

    fun validGridsDataProvider(): Stream<Arguments> = Stream.of(
        Arguments.of(
            5, 100, 5,
            20
        ),
        Arguments.of(
            10, 100, 100,
            100
        ),
        Arguments.of(
            5, 100, 100,
            400
        ),
        Arguments.of(
            10, 100, 300,
            300
        ),
    )

    @Test
    fun `When building a mesh, all the cells must be different`() {
        // Arrange
        val builder = GridBuilder(GridConstraintChecker())

        // Act
        val mesh = builder.buildGrid(10, 100, 100)

        // Assert
        assertEquals(100, mesh.cells.size)

        for (i in 0..9) {
            for (j in 0..9) {
                if (i == j) {
                    continue
                }

                assertNotEquals(mesh.cells[i], mesh.cells[j])
            }
        }
    }
}
