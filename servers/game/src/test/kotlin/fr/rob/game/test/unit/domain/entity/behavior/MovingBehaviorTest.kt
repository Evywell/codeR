package fr.rob.game.test.unit.domain.entity.behavior

import fr.rob.game.domain.entity.Movement
import fr.rob.game.domain.entity.Position
import fr.rob.game.domain.entity.Unit
import fr.rob.game.domain.entity.behavior.MovingBehavior
import fr.rob.game.domain.entity.guid.ObjectGuid
import fr.rob.game.domain.entity.guid.ObjectGuidGenerator
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MovingBehaviorTest {
    @ParameterizedTest
    @MethodSource("orientationsDataProvider")
    fun `As moving behavior, I should calculate positions correctly`(
        startPosition: Position,
        orientation: Float,
        speed: Float,
        timeElapsedMs: Int,
        expectedEndPosition: Position
    ) {
        // Arrange
        val guidGenerator = ObjectGuidGenerator()
        val unit = Unit(
            guidGenerator.fromGuidInfo(ObjectGuidGenerator.GuidInfo(ObjectGuid.LowGuid(1u, 0u), ObjectGuid.GUID_TYPE.GAME_OBJECT)),
            "The unit name",
            1
        )
        val movingBehavior = MovingBehavior(unit)
        val movement = Movement(Movement.MovementDirectionType.FORWARD, orientation, speed)
        unit.position = startPosition

        // Act
        unit.setMoving(movingBehavior, movement)
        unit.update(timeElapsedMs)

        // Assert
        assertEquals(expectedEndPosition.x, unit.position.x)
        assertEquals(expectedEndPosition.y, unit.position.y)
        assertEquals(expectedEndPosition.z, unit.position.z)
        assertEquals(expectedEndPosition.orientation, unit.position.orientation)
    }

    fun orientationsDataProvider(): Stream<Arguments> = Stream.of(
        // Go full EAST from origin
        Arguments.of(originPosition(), 0f, 30f, 1000, Position(30f, 0f, 0f, 0f)),
        // Go full EAST not from origin
        Arguments.of(Position(30f, 0f, 0f, 0f), 0f, 30f, 1000, Position(60f, 0f, 0f, 0f)),
        // Go full NORTH from origin
        Arguments.of(originPosition(), 90f, 30f, 1000, Position(0f, 30f, 0f, 90f)),
        // Go NORTH/EAST from origin
        Arguments.of(originPosition(), 45f, 15f, 1000, Position(10.606f, 10.606f, 0f, 45f)),
        // Go full SOUTH from origin (-0.003f because of radians rounding)
        Arguments.of(originPosition(), 270f, 28f, 1000, Position(-0.003f, -28f, 0f, 270f)),
        // Go full SOUTH not from origin
        Arguments.of(Position(7f, 4f, 0f, 5f), 270f, 28f, 1000, Position(6.997f, -24f, 0f, 270f)),
        // Go full SOUTH not from origin
        Arguments.of(Position(7f, -140f, 0f, 5f), 270f, 28f, 1000, Position(6.997f, -168f, 0f, 270f))
    )

    private fun originPosition(): Position = Position(0f, 0f, 0f, 0f)
}
