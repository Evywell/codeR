package fr.rob.game.test.unit.domain.game.world.entity.guid

import fr.rob.game.domain.entity.guid.ObjectGuid
import fr.rob.game.domain.entity.guid.ObjectGuid.GUID_TYPE
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ObjectGuidTest {

    @Test
    fun `As ObjectGuid, I should provide correct info for high, entry and counter`() {
        // Arrange
        val high = 1048613 // (0000 0001) (0000 0000 0000 0010 0101) => type: 0b1 (1), instance: 0b100101 (37) => 28 bits
        val low = 63373314u // (0000 0000 0011 1100 0111) (0000 0000 0000 0010) => entry: 0b1111000111 (967), counter: 0b10 (2) => 36 bits
        val guid = ObjectGuid(low, high) // (0000 0001) (0000 0000 0000 0010 0101) (0000 0000 0011 1100 0111) (0000 0000 0000 0010): 72060136721940482

        // Act & Assert
        assertEquals(72060136721940482, guid.getRawValue())
        assertEquals(high, guid.getHigh())
        assertEquals(967u, guid.getEntry())
        assertEquals(2u, guid.getCounter())
    }

    @Test
    fun `As ObjectGuid, I should tell if I'm for player`() {
        // Arrange & Act
        val playerGuid = ObjectGuid(1u, GUID_TYPE.PLAYER.value)
        val gameObjectGuid = ObjectGuid(1u, GUID_TYPE.GAME_OBJECT.value)

        // Assert
        assertTrue(playerGuid.isPlayer())
        assertFalse(playerGuid.isGameObject())

        assertTrue(gameObjectGuid.isGameObject())
        assertFalse(gameObjectGuid.isPlayer())

        assertNotEquals(playerGuid, gameObjectGuid)
    }

    @ParameterizedTest
    @MethodSource("worldObjectTypeProvider")
    fun `As valid world object type, I should return the right type`(guidType: GUID_TYPE) {
        // Arrange
        val guid = ObjectGuid(0u, guidType.value)

        // Act
        val type = guid.getType()

        // Assert
        assertEquals(guidType, type)
    }

    fun worldObjectTypeProvider(): Stream<Arguments> = Stream.of(
        Arguments.of(GUID_TYPE.GAME_OBJECT),
        Arguments.of(GUID_TYPE.PLAYER),
    )

    @Test
    fun `As invalid world object type, when reading type, an exception should be thrown`() {
        // Arrange
        val guid = ObjectGuid(0u, -1)

        // Act & Assert
        assertThrows(RuntimeException::class.java) {
            guid.getType()
        }
    }
}
