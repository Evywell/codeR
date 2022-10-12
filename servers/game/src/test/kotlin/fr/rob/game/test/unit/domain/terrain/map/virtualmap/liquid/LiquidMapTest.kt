package fr.rob.game.test.unit.domain.terrain.map.virtualmap.liquid

import fr.rob.game.domain.terrain.map.virtualmap.liquid.LiquidMap
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class LiquidMapTest {
    @ParameterizedTest
    @MethodSource("entityDataProvider")
    fun `As entity in water, I should be tagged as swimming`(x: Float, y: Float, z: Float) {
        // Arrange
        val liquidMap = LiquidMap()

        // Act
        val status = liquidMap.statusForEntity(x, y, z)

        // Assert
        assertTrue(status.isUnderWater)
    }

    fun entityDataProvider(): Stream<Arguments> = Stream.of(
        Arguments.of(1, 1, 1)
    )
}
