package fr.rob.game.test.unit.domain.game.world

import fr.rob.game.entity.guid.ObjectGuid
import fr.rob.game.entity.guid.ObjectGuid.LowGuid
import fr.rob.game.entity.guid.ObjectGuidGenerator
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class ObjectGuidGeneratorTest {

    @Test
    fun `As generator, I should generate unique identifier from map instance`() {
        // Arrange
        val generator = ObjectGuidGenerator()
        val firstGuidInfo = ObjectGuidGenerator.GuidInfo(LowGuid(0u, 1u), ObjectGuid.GUID_TYPE.GAME_OBJECT)
        val secondGuidInfo = ObjectGuidGenerator.GuidInfo(LowGuid(0u, 2u), ObjectGuid.GUID_TYPE.GAME_OBJECT)

        // Act
        val guid1 = generator.fromGuidInfo(firstGuidInfo)
        val guid2 = generator.fromGuidInfo(secondGuidInfo)

        // Assert
        assertNotEquals(guid1, guid2)
    }

    @Test
    fun `As generator, I should generate the same identifier if parameters are identical`() {
        // Arrange
        val generator = ObjectGuidGenerator()
        val guidInfo = ObjectGuidGenerator.GuidInfo(LowGuid(0u, 1u), ObjectGuid.GUID_TYPE.GAME_OBJECT)

        // Act
        val guid1 = generator.fromGuidInfo(guidInfo)
        val guid2 = generator.fromGuidInfo(guidInfo)

        // Assert
        assertEquals(guid1, guid2)
    }

    @Test
    fun `As generator, I should generate identifier for player`() {
        // Arrange
        val generator = ObjectGuidGenerator()

        // Act
        val guid = generator.createForPlayer(554)

        // Assert
        assertTrue(guid.isPlayer())
        assertEquals(554, guid.getCounter().toInt())
    }
}
