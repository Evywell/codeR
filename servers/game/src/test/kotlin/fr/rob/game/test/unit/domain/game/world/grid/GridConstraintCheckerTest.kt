package fr.rob.game.test.unit.domain.game.world.grid

import fr.rob.game.map.grid.GridConstraintChecker
import fr.rob.game.map.grid.exception.NegativeOrNullCellSizeException
import fr.rob.game.map.grid.exception.ZoneDimensionInvalidException
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class GridConstraintCheckerTest {

    @Test
    fun `When checking a grid with negative cell size, an exception must be thrown`() {
        // Arrange
        val checker = GridConstraintChecker()

        // Act & Assert
        assertThrows(NegativeOrNullCellSizeException::class.java) {
            checker.checkGridConfiguration(-3, 10, 10)
        }

        assertThrows(NegativeOrNullCellSizeException::class.java) {
            checker.checkGridConfiguration(0, 10, 10)
        }
    }

    @Test
    fun `When providing a width not divisible by the cell size, an exception must be thrown`() {
        // Arrange
        val checker = GridConstraintChecker()

        // Act & Assert
        assertThrows(ZoneDimensionInvalidException::class.java) {
            checker.checkGridConfiguration(3, 100, 30)
        }
    }

    @Test
    fun `When providing an height not divisible by the cell size, an exception must be thrown`() {
        // Arrange
        val checker = GridConstraintChecker()

        // Act & Assert
        assertThrows(ZoneDimensionInvalidException::class.java) {
            checker.checkGridConfiguration(3, 30, 100)
        }
    }
}
