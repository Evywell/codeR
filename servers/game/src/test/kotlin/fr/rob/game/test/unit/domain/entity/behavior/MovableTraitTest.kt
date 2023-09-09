package fr.rob.game.test.unit.domain.entity.behavior

import fr.rob.game.domain.entity.Movement
import fr.rob.game.domain.entity.Position
import fr.rob.game.domain.entity.Unit
import fr.rob.game.domain.entity.behavior.MovableTrait
import fr.rob.game.domain.entity.guid.ObjectGuid
import fr.rob.game.domain.entity.guid.ObjectGuidGenerator
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MovableTraitTest {
    @ParameterizedTest
    @MethodSource("orientationsDataProvider")
    fun `As movable trait, I should calculate positions correctly`(
        startPosition: Position,
        direction: Movement.MovementDirectionType,
        orientationRadians: Float,
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
        val movableTrait = MovableTrait(unit, speed)
        val movement = Movement(direction, orientationRadians)
        unit.position = startPosition

        // Act
        movableTrait.move(movement)
        movableTrait.update(timeElapsedMs)

        // Assert
        assertEquals(expectedEndPosition.x, unit.position.x)
        assertEquals(expectedEndPosition.y, unit.position.y)
        assertEquals(expectedEndPosition.z, unit.position.z)
        assertEquals(expectedEndPosition.orientation, unit.position.orientation)
    }

    fun orientationsDataProvider(): Stream<Arguments> = Stream.of(
        //region FORWARD
        // Go full EAST from origin
        Arguments.of(originPosition(), Movement.MovementDirectionType.FORWARD, DEG_0_TO_RADIANS, 30f, 1000, Position(30f, 0f, 0f, DEG_0_TO_RADIANS)),
        // Go full EAST not from origin
        Arguments.of(Position(30f, 0f, 0f, 0f), Movement.MovementDirectionType.FORWARD, DEG_0_TO_RADIANS, 30f, 1000, Position(60f, 0f, 0f, DEG_0_TO_RADIANS)),
        // Go full NORTH from origin
        Arguments.of(originPosition(), Movement.MovementDirectionType.FORWARD, DEG_90_TO_RADIANS, 30f, 1000, Position(0f, 30f, 0f, DEG_90_TO_RADIANS)),
        // Go NORTH/EAST from origin
        Arguments.of(originPosition(), Movement.MovementDirectionType.FORWARD, DEG_45_TO_RADIANS, 15f, 1000, Position(10.606f, 10.606f, 0f, DEG_45_TO_RADIANS)),
        // Go full SOUTH from origin
        Arguments.of(originPosition(), Movement.MovementDirectionType.FORWARD, DEG_270_To_RADIANS, 28f, 1000, Position(0f, -28f, 0f, DEG_270_To_RADIANS)),
        // Go full SOUTH not from origin
        Arguments.of(Position(7f, 4f, 0f, 0.0872665f), Movement.MovementDirectionType.FORWARD, DEG_270_To_RADIANS, 28f, 1000, Position(7f, -24f, 0f, DEG_270_To_RADIANS)),
        // Go full SOUTH not from origin
        Arguments.of(Position(7f, -140f, 0f, 0.0872665f), Movement.MovementDirectionType.FORWARD, DEG_270_To_RADIANS, 28f, 1000, Position(7f, -168f, 0f, DEG_270_To_RADIANS)),
        //endregion
        //region BACKWARD
        // Go full EAST from origin
        Arguments.of(originPosition(), Movement.MovementDirectionType.BACKWARD, DEG_0_TO_RADIANS, 30f, 1000, Position(-30f, 0f, 0f, DEG_0_TO_RADIANS)),
        // Go full EAST not from origin
        Arguments.of(Position(-30f, 0f, 0f, 0f), Movement.MovementDirectionType.BACKWARD, DEG_0_TO_RADIANS, 30f, 1000, Position(-60f, 0f, 0f, DEG_0_TO_RADIANS)),
        // Go full NORTH from origin
        Arguments.of(originPosition(), Movement.MovementDirectionType.BACKWARD, DEG_90_TO_RADIANS, 30f, 1000, Position(0f, -30f, 0f, DEG_90_TO_RADIANS)),
        // Go NORTH/EAST from origin
        Arguments.of(originPosition(), Movement.MovementDirectionType.BACKWARD, DEG_45_TO_RADIANS, 15f, 1000, Position(-10.606f, -10.606f, 0f, DEG_45_TO_RADIANS)),
        // Go full SOUTH from origin
        Arguments.of(originPosition(), Movement.MovementDirectionType.BACKWARD, DEG_270_To_RADIANS, 28f, 1000, Position(0f, 28f, 0f, DEG_270_To_RADIANS)),
        // Go full SOUTH not from origin
        Arguments.of(Position(7f, 4f, 0f, 0.0872665f), Movement.MovementDirectionType.BACKWARD, DEG_270_To_RADIANS, 28f, 1000, Position(7f, 32f, 0f, DEG_270_To_RADIANS)),
        // Go full SOUTH not from origin
        Arguments.of(Position(7f, -140f, 0f, 0.0872665f), Movement.MovementDirectionType.BACKWARD, DEG_270_To_RADIANS, 28f, 1000, Position(7f, -112f, 0f, DEG_270_To_RADIANS)),
        //endregion
        //region LEFT
        // Go full EAST from origin
        Arguments.of(originPosition(), Movement.MovementDirectionType.LEFT, DEG_0_TO_RADIANS, 30f, 1000, Position(0f, 30f, 0f, DEG_0_TO_RADIANS)),
        // Go full EAST not from origin
        Arguments.of(Position(30f, 0f, 0f, 0f), Movement.MovementDirectionType.LEFT, DEG_0_TO_RADIANS, 30f, 1000, Position(30f, 30f, 0f, DEG_0_TO_RADIANS)),
        // Go full NORTH from origin
        Arguments.of(originPosition(), Movement.MovementDirectionType.LEFT, DEG_90_TO_RADIANS, 30f, 1000, Position(-30f, 0f, 0f, DEG_90_TO_RADIANS)),
        // Go NORTH/EAST from origin
        Arguments.of(originPosition(), Movement.MovementDirectionType.LEFT, DEG_45_TO_RADIANS, 15f, 1000, Position(-10.606f, 10.606f, 0f, DEG_45_TO_RADIANS)),
        // Go full SOUTH from origin
        Arguments.of(originPosition(), Movement.MovementDirectionType.LEFT, DEG_270_To_RADIANS, 28f, 1000, Position(28f, 0f, 0f, DEG_270_To_RADIANS)),
        // Go full SOUTH not from origin
        Arguments.of(Position(7f, 4f, 0f, 0.0872665f), Movement.MovementDirectionType.LEFT, DEG_270_To_RADIANS, 28f, 1000, Position(35f, 4f, 0f, DEG_270_To_RADIANS)),
        // Go full SOUTH not from origin
        Arguments.of(Position(-7f, -140f, 0f, 0.0872665f), Movement.MovementDirectionType.LEFT, DEG_270_To_RADIANS, 28f, 1000, Position(21f, -140f, 0f, DEG_270_To_RADIANS)),
        //endregion
        //region RIGHT
        // Go full EAST from origin
        Arguments.of(originPosition(), Movement.MovementDirectionType.RIGHT, DEG_0_TO_RADIANS, 30f, 1000, Position(0f, -30f, 0f, DEG_0_TO_RADIANS)),
        // Go full EAST not from origin
        Arguments.of(Position(30f, 0f, 0f, 0f), Movement.MovementDirectionType.RIGHT, DEG_0_TO_RADIANS, 30f, 1000, Position(30f, -30f, 0f, DEG_0_TO_RADIANS)),
        // Go full NORTH from origin
        Arguments.of(originPosition(), Movement.MovementDirectionType.RIGHT, DEG_90_TO_RADIANS, 30f, 1000, Position(30f, 0f, 0f, DEG_90_TO_RADIANS)),
        // Go NORTH/EAST from origin
        Arguments.of(originPosition(), Movement.MovementDirectionType.RIGHT, DEG_45_TO_RADIANS, 15f, 1000, Position(10.606f, -10.606f, 0f, DEG_45_TO_RADIANS)),
        // Go full SOUTH from origin
        Arguments.of(originPosition(), Movement.MovementDirectionType.RIGHT, DEG_270_To_RADIANS, 28f, 1000, Position(-28f, 0f, 0f, DEG_270_To_RADIANS)),
        // Go full SOUTH not from origin
        Arguments.of(Position(7f, 4f, 0f, 0.0872665f), Movement.MovementDirectionType.RIGHT, DEG_270_To_RADIANS, 28f, 1000, Position(-21f, 4f, 0f, DEG_270_To_RADIANS)),
        // Go full SOUTH not from origin
        Arguments.of(Position(-7f, -140f, 0f, 0.0872665f), Movement.MovementDirectionType.RIGHT, DEG_270_To_RADIANS, 28f, 1000, Position(-35f, -140f, 0f, DEG_270_To_RADIANS))
        //endregion
    )

    private fun originPosition(): Position = Position(0f, 0f, 0f, 0f)

    companion object {
        private const val DEG_0_TO_RADIANS = 0f
        private const val DEG_45_TO_RADIANS = 0.7853982f
        private const val DEG_90_TO_RADIANS = 1.5707964f
        private const val DEG_270_To_RADIANS = 4.712389f
    }
}
