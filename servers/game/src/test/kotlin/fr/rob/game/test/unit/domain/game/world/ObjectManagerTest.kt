package fr.rob.game.test.unit.domain.game.world

import fr.rob.game.domain.entity.ObjectManager
import fr.rob.game.domain.entity.Position
import fr.rob.game.domain.entity.exception.OutOfBoundsException
import fr.rob.game.domain.entity.guid.ObjectGuid
import fr.rob.game.domain.entity.guid.ObjectGuidGenerator
import fr.rob.game.domain.instance.MapInstance
import fr.rob.game.domain.terrain.grid.Grid
import fr.rob.game.domain.terrain.grid.GridBuilder
import fr.rob.game.domain.terrain.grid.GridConstraintChecker
import fr.rob.game.domain.terrain.map.Map
import fr.rob.game.domain.terrain.map.MapInfo
import fr.rob.game.domain.terrain.map.ZoneInfo
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ObjectManagerTest {
    @Test
    fun `As OM, I should successfully create an object in a specific instance`() {
        // Arrange
        val om = getObjectManager()
        val mapInfo = MapInfo("A testing map", 200, 200)
        val zoneInfo = ZoneInfo("A testing zone", 100, 100, 0f, 0f)
        val map = Map(1, 1, mapInfo, zoneInfo)
        val gridBuilder = GridBuilder(GridConstraintChecker())
        val grid = gridBuilder.buildGrid(10, zoneInfo.width, zoneInfo.height)
        val instance = MapInstance(13, map, grid)
        val position = Position(10f, 0f, 15f, 0f)
        val lowGuid = ObjectGuid.LowGuid(1u, 1u)

        // Act
        val obj = om.spawnObject(lowGuid, position, instance).get()

        // Assert
        assertTrue(obj.isInWorld)
        assertEquals(13, obj.mapInstance.id)

        assertEquals(10f, obj.position.x)
        assertEquals(0f, obj.position.y)
        assertEquals(15f, obj.position.z)
        assertEquals(0f, obj.position.orientation)
    }

    @ParameterizedTest
    @MethodSource("outOfBoundsDataProvider")
    fun `I should not be able to create an object if it's out of bounds of the map`(zoneInfo: ZoneInfo, position: Position) {
        // Arrange
        val om = getObjectManager()
        val mapInfo = MapInfo("A testing map", 200, 200)
        val map = Map(1, 1, mapInfo, zoneInfo)
        val grid = Grid(100, 100, 10, emptyArray())
        val instance = MapInstance(13, map, grid)
        val lowGuid = ObjectGuid.LowGuid(1u, 1u)

        // Act & Assert
        assertThrows(OutOfBoundsException::class.java) { om.spawnObject(lowGuid, position, instance) }
    }

    fun outOfBoundsDataProvider(): Stream<Arguments> = Stream.of(
        Arguments.of(
            ZoneInfo("A testing zone", 100, 100, 0f, 0f),
            Position(800f, 0f, 15f, 0f) // X too big
        ),
        Arguments.of(
            ZoneInfo("A testing zone", 100, 100, 0f, 0f),
            Position(-10f, 7f, 15f, 0f) // Negative X
        ),
        Arguments.of(
            ZoneInfo("A testing zone", 100, 100, 0f, 0f),
            Position(5f, 789f, 15f, 0f) // Y too big
        ),
        Arguments.of(
            ZoneInfo("A testing zone", 100, 100, 0f, 0f),
            Position(5f, -61f, 15f, 0f) // Negative Y
        ),
        Arguments.of(
            ZoneInfo("A testing zone", 100, 100, -100f, 100f),
            Position(5f, -61f, 15f, 0f) // X too big with offset -100
        )
    )

    private fun getObjectManager(): ObjectManager = ObjectManager(ObjectGuidGenerator())
}
