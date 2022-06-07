package fr.rob.game.test.unit.domain.game.world

import fr.rob.game.game.world.entity.guid.ObjectGuid
import fr.rob.game.game.world.entity.guid.ObjectGuid.LowGuid
import fr.rob.game.game.world.entity.guid.ObjectGuidGenerator
import fr.rob.game.game.world.instance.MapInstance
import fr.rob.game.game.world.map.Map
import fr.rob.game.game.world.map.MapInfo
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test

class ObjectGuidGeneratorTest {

    @Test
    fun `As generator, I should generate unique identifier from map instance`() {
        // Arrange
        val generator = ObjectGuidGenerator()
        val map = Map(1, 2, MapInfo("Testing zone", 10, 10), null)
        val instance = MapInstance(1, map)
        val firstGuidInfo = ObjectGuidGenerator.GuidInfo(LowGuid(0u, 1u), ObjectGuid.GUID_TYPE.GAME_OBJECT)
        val secondGuidInfo = ObjectGuidGenerator.GuidInfo(LowGuid(0u, 2u), ObjectGuid.GUID_TYPE.GAME_OBJECT)

        // Act
        val guid1 = generator.fromMapInstance(firstGuidInfo, instance)
        val guid2 = generator.fromMapInstance(secondGuidInfo, instance)

        // Assert
        assertNotEquals(guid1, guid2)
    }

    @Test
    fun `As generator, I should generate the same identifier if parameters are identical`() {
        // Arrange
        val generator = ObjectGuidGenerator()
        val map = Map(1, 2, MapInfo("Testing zone", 10, 10), null)
        val instance = MapInstance(1, map)
        val guidInfo = ObjectGuidGenerator.GuidInfo(LowGuid(0u, 1u), ObjectGuid.GUID_TYPE.GAME_OBJECT)

        // Act
        val guid1 = generator.fromMapInstance(guidInfo, instance)
        val guid2 = generator.fromMapInstance(guidInfo, instance)

        // Assert
        assertEquals(guid1, guid2)
    }
}
