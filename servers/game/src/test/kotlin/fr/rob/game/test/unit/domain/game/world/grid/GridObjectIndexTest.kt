package fr.rob.game.test.unit.domain.game.world.grid

import fr.rob.game.entity.Position
import fr.rob.game.entity.guid.ObjectGuidGenerator
import fr.rob.game.map.grid.Cell
import fr.rob.game.test.unit.tools.DummyWorldObjectBuilder
import fr.rob.game.test.unit.tools.WorldBuilder
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class GridObjectIndexTest {
    private class TestComponent

    private val worldBuilder = WorldBuilder()

    // 100x100 zone, cell size 10, centered on 0
    private val instance = worldBuilder.buildBasicWorld()
    private val grid = instance.grid
    private val objectBuilder = DummyWorldObjectBuilder(ObjectGuidGenerator())

    @Test
    fun `an object moving to another cell is re-indexed`() {
        val unit = objectBuilder.createUnit()
        worldBuilder.addIntoInstance(unit, instance, Position(1f, 1f, 0f, 0f))
        val startCell = grid.getRegisteredCell(unit)!!

        unit.setPosition(35f, 35f, 0f, 0f)
        val newCell = grid.getRegisteredCell(unit)!!

        assertEquals(grid.getCellFromWorldPosition(unit.position), newCell)
        assertFalse(grid.getObjectsOfCell(startCell).contains(unit))
        assertTrue(grid.getObjectsOfCell(newCell).contains(unit))
    }

    @Test
    fun `component queries follow a moved object`() {
        val unit = objectBuilder.createUnit()
        unit.addComponent(TestComponent())
        worldBuilder.addIntoInstance(unit, instance, Position(1f, 1f, 0f, 0f))

        unit.setPosition(35f, 35f, 0f, 0f)

        assertTrue(grid.findObjectsWithComponentInRadius(Position(1f, 1f, 0f, 0f), 5f, TestComponent::class).isEmpty())
        assertEquals(listOf(unit), grid.findObjectsWithComponentInRadius(Position(35f, 35f, 0f, 0f), 5f, TestComponent::class))
    }

    @Test
    fun `removing a moved object clears every index`() {
        val unit = objectBuilder.createUnit()
        unit.addComponent(TestComponent())
        worldBuilder.addIntoInstance(unit, instance, Position(1f, 1f, 0f, 0f))
        unit.setPosition(35f, 35f, 0f, 0f)

        grid.removeWorldObject(unit)

        assertNull(grid.getRegisteredCell(unit))
        assertTrue(grid.cells.none { grid.getObjectsOfCell(it).contains(unit) })
        assertTrue(grid.findObjectsWithComponentInRadius(Position(35f, 35f, 0f, 0f), 5f, TestComponent::class).isEmpty())
    }

    @Test
    fun `a position outside the grid keeps the previous cell`() {
        val unit = objectBuilder.createUnit()
        worldBuilder.addIntoInstance(unit, instance, Position(1f, 1f, 0f, 0f))
        val startCell = grid.getRegisteredCell(unit)

        unit.setPosition(Float.NaN, 1f, 0f, 0f)

        assertEquals(startCell, grid.getRegisteredCell(unit))
    }

    @Test
    fun `radius query spans as many cells as the radius covers`() {
        val near = objectBuilder.createUnit()
        val twoCellsAway = objectBuilder.createUnit()
        val outside = objectBuilder.createUnit()
        worldBuilder.addIntoInstance(near, instance, Position(1f, 1f, 0f, 0f))
        worldBuilder.addIntoInstance(twoCellsAway, instance, Position(19f, 1f, 0f, 0f))
        worldBuilder.addIntoInstance(outside, instance, Position(25f, 1f, 0f, 0f))

        val result = grid.findObjectsInsideRadius(Position(1f, 1f, 0f, 0f), 18f).toSet()

        assertEquals(setOf(near, twoCellsAway), result)
    }

    @Test
    fun `players of a cell come from the cell index`() {
        val player = objectBuilder.createPlayer()
        val unit = objectBuilder.createUnit()
        worldBuilder.addIntoInstance(player, instance, Position(1f, 1f, 0f, 0f))
        worldBuilder.addIntoInstance(unit, instance, Position(2f, 2f, 0f, 0f))

        val cell = grid.getRegisteredCell(player)!!

        assertEquals(listOf(player), grid.getPlayersOfCell(cell))
        assertTrue(grid.getPlayersOfCell(Cell(0, 0)).isEmpty())
    }
}
