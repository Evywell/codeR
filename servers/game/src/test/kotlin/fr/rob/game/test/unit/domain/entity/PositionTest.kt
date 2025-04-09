package fr.rob.game.test.unit.domain.entity

import fr.rob.game.domain.entity.Position
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.math.PI

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PositionTest {
    @ParameterizedTest
    @MethodSource("inFrontOfPositionDataProvider")
    fun `Position in front of me should be in arc`(
        source: Position,
        target: Position,
    ) {
        assertTrue(source.hasInArc(PI.toFloat(), target))
    }

    fun inFrontOfPositionDataProvider(): Stream<Arguments> =
        Stream.of(
            //region EAST oriented from the center
            Arguments.of(
                Position(0f, 0f, 0f, 0f),
                Position(10f, 10f, 0f, 0f),
            ),
            Arguments.of(
                Position(0f, 0f, 0f, 0f),
                Position(10f, -10f, 0f, 0f),
            ),
            Arguments.of(
                Position(0f, 0f, 0f, 0f),
                Position(3f, -10f, 0f, 0f),
            ),
            //endregion
            //region EAST oriented from negative coordinates
            Arguments.of(
                Position(-5f, 8f, 0f, 0f),
                Position(10f, 10f, 0f, 0f),
            ),
            Arguments.of(
                Position(-5f, 8f, 0f, 0f),
                Position(10f, -10f, 0f, 0f),
            ),
            Arguments.of(
                Position(-5f, 8f, 0f, 0f),
                Position(-4f, -2f, 0f, 0f),
            ),
            //endregion
            //region WEST oriented from the center
            Arguments.of(
                Position(0f, 0f, 0f, 3.14f),
                Position(-10f, 10f, 0f, 0f),
            ),
            Arguments.of(
                Position(0f, 0f, 0f, 3.14f),
                Position(-10f, -10f, 0f, 0f),
            ),
            Arguments.of(
                Position(0f, 0f, 0f, 3.14f),
                Position(-3f, -10f, 0f, 0f),
            ),
            //endregion
            //region WEST oriented from negative coordinates
            Arguments.of(
                Position(-5f, 8f, 0f, 3.14f),
                Position(-10f, 10f, 0f, 0f),
            ),
            Arguments.of(
                Position(-5f, 8f, 0f, 3.14f),
                Position(-10f, -10f, 0f, 0f),
            ),
            Arguments.of(
                Position(-5f, 8f, 0f, 3.14f),
                Position(-6f, -2f, 0f, 0f),
            ),
            //endregion
            //region SOUTH oriented from the center
            Arguments.of(
                Position(0f, 0f, 0f, 4.712389f),
                Position(-10f, -10f, 0f, 0f),
            ),
            Arguments.of(
                Position(0f, 0f, 0f, 4.712389f),
                Position(-10f, -10f, 0f, 0f),
            ),
            Arguments.of(
                Position(0f, 0f, 0f, 4.712389f),
                Position(-3f, -10f, 0f, 0f),
            ),
            //endregion
            //region SOUTH oriented from negative coordinates
            Arguments.of(
                Position(-5f, 8f, 0f, 4.712389f),
                Position(10f, -10f, 0f, 0f),
            ),
            Arguments.of(
                Position(-5f, 8f, 0f, 4.712389f),
                Position(10f, -10f, 0f, 0f),
            ),
            Arguments.of(
                Position(-5f, 8f, 0f, 4.712389f),
                Position(-4f, -2f, 0f, 0f),
            ),
            //endregion
        )

    @ParameterizedTest
    @MethodSource("behindPositionDataProvider")
    fun `Position behind me should NOT be in arc`(
        source: Position,
        target: Position,
    ) {
        assertFalse(source.hasInArc(PI.toFloat(), target))
    }

    fun behindPositionDataProvider(): Stream<Arguments> =
        Stream.of(
            //region EAST oriented from the center
            Arguments.of(
                Position(0f, 0f, 0f, 0f),
                Position(-10f, 10f, 0f, 0f),
            ),
            Arguments.of(
                Position(0f, 0f, 0f, 0f),
                Position(-10f, -10f, 0f, 0f),
            ),
            Arguments.of(
                Position(0f, 0f, 0f, 0f),
                Position(-3f, -10f, 0f, 0f),
            ),
            //endregion
            //region EAST oriented from negative coordinates
            Arguments.of(
                Position(-5f, 8f, 0f, 0f),
                Position(-10f, 10f, 0f, 0f),
            ),
            Arguments.of(
                Position(-5f, 8f, 0f, 0f),
                Position(-10f, -10f, 0f, 0f),
            ),
            Arguments.of(
                Position(-5f, 8f, 0f, 0f),
                Position(-6f, -2f, 0f, 0f),
            ),
            //endregion
            //region WEST oriented from the center
            Arguments.of(
                Position(0f, 0f, 0f, 3.14f),
                Position(10f, 10f, 0f, 0f),
            ),
            Arguments.of(
                Position(0f, 0f, 0f, 3.14f),
                Position(10f, -10f, 0f, 0f),
            ),
            Arguments.of(
                Position(0f, 0f, 0f, 3.14f),
                Position(3f, -10f, 0f, 0f),
            ),
            //endregion
            //region WEST oriented from negative coordinates
            Arguments.of(
                Position(-5f, 8f, 0f, 3.14f),
                Position(10f, 10f, 0f, 0f),
            ),
            Arguments.of(
                Position(-5f, 8f, 0f, 3.14f),
                Position(10f, -10f, 0f, 0f),
            ),
            Arguments.of(
                Position(-5f, 8f, 0f, 3.14f),
                Position(-4f, -2f, 0f, 0f),
            ),
            //endregion
            //region SOUTH oriented from the center
            Arguments.of(
                Position(0f, 0f, 0f, 4.712389f),
                Position(-10f, 10f, 0f, 0f),
            ),
            Arguments.of(
                Position(0f, 0f, 0f, 4.712389f),
                Position(-10f, 10f, 0f, 0f),
            ),
            Arguments.of(
                Position(0f, 0f, 0f, 4.712389f),
                Position(-3f, 10f, 0f, 0f),
            ),
            //endregion
            //region SOUTH oriented from negative coordinates
            Arguments.of(
                Position(-5f, 8f, 0f, 4.712389f),
                Position(10f, 10f, 0f, 0f),
            ),
            Arguments.of(
                Position(-5f, 8f, 0f, 4.712389f),
                Position(10f, 10f, 0f, 0f),
            ),
            Arguments.of(
                Position(-5f, 8f, 0f, 4.712389f),
                Position(-4f, 9f, 0f, 0f),
            ),
            //endregion
        )

    @ParameterizedTest
    @MethodSource("positionInsideArcDataProvider")
    fun `Position inside arc`(
        arcAngle: Float,
        source: Position,
        target: Position,
    ) {
        assertTrue(source.hasInArc(arcAngle, target))
    }

    fun positionInsideArcDataProvider(): Stream<Arguments> =
        Stream.of(
            Arguments.of(
                (2 * PI).toFloat() / 3, // 120°
                Position(0f, 0f, 0f, 0f),
                Position(10f, 0f, 0f, 0f),
            ),
            Arguments.of(
                (2 * PI).toFloat() / 3, // 120°
                Position(0f, 0f, 0f, 0f),
                Position(8f, 10f, 0f, 0f),
            ),
            Arguments.of(
                (2 * PI).toFloat() / 3, // 120°
                Position(0f, 0f, 0f, 0f),
                Position(2.5f, -4f, 0f, 0f),
            ),
            Arguments.of(
                1.13446f, // 65°
                Position(147f, -90f, 0f, 4.17134f),
                Position(25f, -185f, 0f, 0f),
            ),
        )

    @ParameterizedTest
    @MethodSource("positionOutsideArcDataProvider")
    fun `Position outside arc`(
        arcAngle: Float,
        source: Position,
        target: Position,
    ) {
        assertFalse(source.hasInArc(arcAngle, target))
    }

    fun positionOutsideArcDataProvider(): Stream<Arguments> =
        Stream.of(
            Arguments.of(
                (2 * PI).toFloat() / 3, // 120°
                Position(0f, 0f, 0f, 0f),
                Position(2f, -6f, 0f, 0f),
            ),
            Arguments.of(
                (2 * PI).toFloat() / 3, // 120°
                Position(0f, 0f, 0f, 0f),
                Position(4f, 8f, 0f, 0f),
            ),
            Arguments.of(
                (2 * PI).toFloat() / 3, // 120°
                Position(0f, 0f, 0f, 0f),
                Position(2.5f, -4.8f, 0f, 0f),
            ),
            Arguments.of(
                1.13446f, // 65°
                Position(147f, -90f, 0f, 4.17134f),
                Position(157f, -193f, 0f, 0f),
            ),
        )
}
