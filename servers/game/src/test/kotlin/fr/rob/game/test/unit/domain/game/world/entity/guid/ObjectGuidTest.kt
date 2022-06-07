package fr.rob.game.test.unit.domain.game.world.entity.guid

import fr.rob.game.game.world.entity.guid.ObjectGuid
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ObjectGuidTest {

    @Test
    fun `As ObjectGuid, I should provide correct info for high, entry and counter`() {
        // Arrange
        val high = 1048613u // (0000 0001) (0000 0000 0000 0010 0101) => type: 0b1 (1), instance: 0b100101 (37) => 28 bits
        val low = 63373314u // (0000 0000 0011 1100 0111) (0000 0000 0000 0010) => entry: 0b1111000111 (967), counter: 0b10 (2) => 36 bits
        val guid = ObjectGuid(low, high) // (0000 0001) (0000 0000 0000 0010 0101) (0000 0000 0011 1100 0111) (0000 0000 0000 0010): 72060136721940482

        // Act & Assert
        assertEquals(72060136721940482u, guid.getRawValue())
        assertEquals(high, guid.getHigh())
        assertEquals(967u, guid.getEntry())
        assertEquals(2u, guid.getCounter())
    }
}
