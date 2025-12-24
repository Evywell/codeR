package fr.rob.game.test.unit.domain.game.world.grid

import fr.rob.game.entity.Position
import fr.rob.game.entity.WorldObject
import fr.rob.game.entity.guid.ObjectGuidGenerator
import fr.rob.game.map.grid.Cell
import fr.rob.game.map.grid.GridBuilder
import fr.rob.game.map.grid.GridConstraintChecker
import fr.rob.game.test.unit.tools.DummyWorldObjectBuilder
import fr.rob.game.test.unit.tools.WorldBuilder
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

// Test component classes
class TestComponentA
class TestComponentB
class TestComponentC

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

    @Test
    fun `findObjectsWithComponentInRadius should return only objects with specified component within radius`() {
        // Arrange
        val instance = WorldBuilder.buildBasicWorld()
        val objectBuilder = DummyWorldObjectBuilder(ObjectGuidGenerator())

        // Create objects with ComponentA
        val objectWithA1 = objectBuilder.createUnit()
        objectWithA1.addComponent(TestComponentA())
        objectWithA1.addIntoInstance(instance, Position(5f, 5f, 0f, 0f))

        val objectWithA2 = objectBuilder.createUnit()
        objectWithA2.addComponent(TestComponentA())
        objectWithA2.addIntoInstance(instance, Position(8f, 5f, 0f, 0f))

        // Create objects with ComponentB (not A)
        val objectWithB = objectBuilder.createUnit()
        objectWithB.addComponent(TestComponentB())
        objectWithB.addIntoInstance(instance, Position(6f, 6f, 0f, 0f))

        // Create object with ComponentA but outside radius
        val objectWithAOutside = objectBuilder.createUnit()
        objectWithAOutside.addComponent(TestComponentA())
        objectWithAOutside.addIntoInstance(instance, Position(50f, 50f, 0f, 0f))

        val origin = Position(5f, 5f, 0f, 0f)
        val radius = 5f

        // Act
        val result = instance.grid.findObjectsWithComponentInRadius(origin, radius, TestComponentA::class)

        // Assert
        Assertions.assertEquals(2, result.size)
        Assertions.assertTrue(result.contains(objectWithA1))
        Assertions.assertTrue(result.contains(objectWithA2))
        Assertions.assertFalse(result.contains(objectWithB))
        Assertions.assertFalse(result.contains(objectWithAOutside))
    }

    @Test
    fun `findObjectsWithComponentInRadius should return empty list when no objects have the component`() {
        // Arrange
        val instance = WorldBuilder.buildBasicWorld()
        val objectBuilder = DummyWorldObjectBuilder(ObjectGuidGenerator())

        val objectWithB = objectBuilder.createUnit()
        objectWithB.addComponent(TestComponentB())
        objectWithB.addIntoInstance(instance, Position(5f, 5f, 0f, 0f))

        val origin = Position(5f, 5f, 0f, 0f)
        val radius = 10f

        // Act
        val result = instance.grid.findObjectsWithComponentInRadius(origin, radius, TestComponentA::class)

        // Assert
        Assertions.assertTrue(result.isEmpty())
    }

    @Test
    fun `findObjectsWithComponentInRadius should return empty list when objects are outside radius`() {
        // Arrange
        val instance = WorldBuilder.buildBasicWorld()
        val objectBuilder = DummyWorldObjectBuilder(ObjectGuidGenerator())

        val objectWithA = objectBuilder.createUnit()
        objectWithA.addComponent(TestComponentA())
        objectWithA.addIntoInstance(instance, Position(50f, 50f, 0f, 0f))

        val origin = Position(5f, 5f, 0f, 0f)
        val radius = 5f

        // Act
        val result = instance.grid.findObjectsWithComponentInRadius(origin, radius, TestComponentA::class)

        // Assert
        Assertions.assertTrue(result.isEmpty())
    }

    @Test
    fun `findObjectsWithComponentsInRadius should return only objects with all specified components within radius`() {
        // Arrange
        val instance = WorldBuilder.buildBasicWorld()
        val objectBuilder = DummyWorldObjectBuilder(ObjectGuidGenerator())

        // Object with both ComponentA and ComponentB
        val objectWithAB = objectBuilder.createUnit()
        objectWithAB.addComponent(TestComponentA())
        objectWithAB.addComponent(TestComponentB())
        objectWithAB.addIntoInstance(instance, Position(5f, 5f, 0f, 0f))

        // Object with only ComponentA
        val objectWithOnlyA = objectBuilder.createUnit()
        objectWithOnlyA.addComponent(TestComponentA())
        objectWithOnlyA.addIntoInstance(instance, Position(6f, 6f, 0f, 0f))

        // Object with only ComponentB
        val objectWithOnlyB = objectBuilder.createUnit()
        objectWithOnlyB.addComponent(TestComponentB())
        objectWithOnlyB.addIntoInstance(instance, Position(7f, 5f, 0f, 0f))

        // Object with both components but outside radius
        val objectWithABOutside = objectBuilder.createUnit()
        objectWithABOutside.addComponent(TestComponentA())
        objectWithABOutside.addComponent(TestComponentB())
        objectWithABOutside.addIntoInstance(instance, Position(50f, 50f, 0f, 0f))

        val origin = Position(5f, 5f, 0f, 0f)
        val radius = 5f

        // Act
        val result = instance.grid.findObjectsWithComponentsInRadius(
            origin,
            radius,
            TestComponentA::class,
            TestComponentB::class
        )

        // Assert
        Assertions.assertEquals(1, result.size)
        Assertions.assertTrue(result.contains(objectWithAB))
        Assertions.assertFalse(result.contains(objectWithOnlyA))
        Assertions.assertFalse(result.contains(objectWithOnlyB))
        Assertions.assertFalse(result.contains(objectWithABOutside))
    }

    @Test
    fun `findObjectsWithComponentsInRadius should return empty list when given no components`() {
        // Arrange
        val instance = WorldBuilder.buildBasicWorld()
        val objectBuilder = DummyWorldObjectBuilder(ObjectGuidGenerator())

        val obj = objectBuilder.createUnit()
        obj.addComponent(TestComponentA())
        obj.addIntoInstance(instance, Position(5f, 5f, 0f, 0f))

        val origin = Position(5f, 5f, 0f, 0f)
        val radius = 10f

        // Act
        val result = instance.grid.findObjectsWithComponentsInRadius(origin, radius)

        // Assert
        Assertions.assertTrue(result.isEmpty())
    }

    @Test
    fun `findObjectsWithComponentsInRadius should delegate to findObjectsWithComponentInRadius when given single component`() {
        // Arrange
        val instance = WorldBuilder.buildBasicWorld()
        val objectBuilder = DummyWorldObjectBuilder(ObjectGuidGenerator())

        val objectWithA = objectBuilder.createUnit()
        objectWithA.addComponent(TestComponentA())
        objectWithA.addIntoInstance(instance, Position(5f, 5f, 0f, 0f))

        val objectWithB = objectBuilder.createUnit()
        objectWithB.addComponent(TestComponentB())
        objectWithB.addIntoInstance(instance, Position(6f, 6f, 0f, 0f))

        val origin = Position(5f, 5f, 0f, 0f)
        val radius = 10f

        // Act
        val result = instance.grid.findObjectsWithComponentsInRadius(origin, radius, TestComponentA::class)

        // Assert
        Assertions.assertEquals(1, result.size)
        Assertions.assertTrue(result.contains(objectWithA))
        Assertions.assertFalse(result.contains(objectWithB))
    }

    @Test
    fun `findObjectsWithComponentsInRadius should handle three or more components correctly`() {
        // Arrange
        val instance = WorldBuilder.buildBasicWorld()
        val objectBuilder = DummyWorldObjectBuilder(ObjectGuidGenerator())

        // Object with all three components
        val objectWithABC = objectBuilder.createUnit()
        objectWithABC.addComponent(TestComponentA())
        objectWithABC.addComponent(TestComponentB())
        objectWithABC.addComponent(TestComponentC())
        objectWithABC.addIntoInstance(instance, Position(5f, 5f, 0f, 0f))

        // Object with only two components
        val objectWithAB = objectBuilder.createUnit()
        objectWithAB.addComponent(TestComponentA())
        objectWithAB.addComponent(TestComponentB())
        objectWithAB.addIntoInstance(instance, Position(6f, 6f, 0f, 0f))

        val origin = Position(5f, 5f, 0f, 0f)
        val radius = 10f

        // Act
        val result = instance.grid.findObjectsWithComponentsInRadius(
            origin,
            radius,
            TestComponentA::class,
            TestComponentB::class,
            TestComponentC::class
        )

        // Assert
        Assertions.assertEquals(1, result.size)
        Assertions.assertTrue(result.contains(objectWithABC))
        Assertions.assertFalse(result.contains(objectWithAB))
    }

    @Test
    fun `findObjectsWithComponentInRadius should respect exact radius boundary`() {
        // Arrange
        val instance = WorldBuilder.buildBasicWorld()
        val objectBuilder = DummyWorldObjectBuilder(ObjectGuidGenerator())

        val origin = Position(0f, 0f, 0f, 0f)
        val radius = 5f

        // Object exactly at radius (distance = 5)
        val objectAtBoundary = objectBuilder.createUnit()
        objectAtBoundary.addComponent(TestComponentA())
        objectAtBoundary.addIntoInstance(instance, Position(3f, 4f, 0f, 0f)) // 3-4-5 triangle

        // Object just inside radius
        val objectInside = objectBuilder.createUnit()
        objectInside.addComponent(TestComponentA())
        objectInside.addIntoInstance(instance, Position(2f, 2f, 0f, 0f))

        // Object just outside radius
        val objectOutside = objectBuilder.createUnit()
        objectOutside.addComponent(TestComponentA())
        objectOutside.addIntoInstance(instance, Position(4f, 4f, 0f, 0f))

        // Act
        val result = instance.grid.findObjectsWithComponentInRadius(origin, radius, TestComponentA::class)

        // Assert
        Assertions.assertTrue(result.contains(objectAtBoundary))
        Assertions.assertTrue(result.contains(objectInside))
        Assertions.assertFalse(result.contains(objectOutside))
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
