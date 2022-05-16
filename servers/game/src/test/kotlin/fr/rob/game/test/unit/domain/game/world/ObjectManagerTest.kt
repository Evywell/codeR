package fr.rob.game.test.unit.domain.game.world

import fr.rob.game.game.world.entity.ObjectManager
import fr.rob.game.game.world.entity.Position
import fr.rob.game.game.world.entity.exception.OutOfBoundsException
import fr.rob.game.game.world.entity.template.WorldObjectTemplate
import fr.rob.game.game.world.instance.MapInstance
import fr.rob.game.game.world.map.Map
import fr.rob.game.game.world.map.MapInfo
import fr.rob.game.game.world.map.ZoneInfo
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class ObjectManagerTest {
    @Test
    fun `As OM, I should successfully create an object in a specific instance`() {
        // Arrange
        val om = ObjectManager()
        val mapInfo = MapInfo("A testing map")
        val zoneInfo = ZoneInfo("A testing zone", 100, 100, 0f, 0f)
        val map = Map(1, 1, mapInfo, zoneInfo)
        val instance = MapInstance(13, map)
        val objTemplate = WorldObjectTemplate(Position(10f, 0f, 15f, 0f))

        // Act
        val obj = om.spawnObject(objTemplate, instance)

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
    fun `I should not be able to create an object if its out of map bounds`(zoneInfo: ZoneInfo, position: Position) {
        // Arrange
        val om = ObjectManager()
        val mapInfo = MapInfo("A testing map")
        val zoneInfo = ZoneInfo("A testing zone", 100, 100, 0f, 0f)
        val map = Map(1, 1, mapInfo, zoneInfo)
        val instance = MapInstance(13, map)
        val objTemplate = WorldObjectTemplate(Position(800f, 0f, 15f, 0f))

        // Act & Assert
        assertThrows(OutOfBoundsException::class.java) { om.spawnObject(objTemplate, instance) }
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
    )
}
