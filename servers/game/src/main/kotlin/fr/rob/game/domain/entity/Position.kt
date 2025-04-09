package fr.rob.game.domain.entity

import kotlin.math.PI
import kotlin.math.atan2

class Position(
    var x: Float,
    var y: Float,
    var z: Float,
    notNormalizedOrientation: Float,
) {
    var orientation: Float = normalizeOrientation(notNormalizedOrientation)
        set(value) {
            field = normalizeOrientation(value)
        }

    fun isSameHas(position: Position): Boolean =
        x == position.x && y == position.y && z == position.z && orientation == position.orientation

    fun getSquaredDistanceWith(position: Position): Float =
        ((x - position.x) * (x - position.x)) + ((y - position.y) * (y - position.y)) + ((z - position.z) * (z - position.z))

    fun hasInArc(
        arc: Float,
        position: Position,
        border: Float = 2f,
    ): Boolean {
        val normalizedArc = normalizeOrientation(arc)

        var angle = getRelativeAngle(position)

        if (angle > PI) {
            angle -= ANGLE_2_PI
        }

        val leftBorder = -1 * (normalizedArc / border)
        val rightBorder = (normalizedArc / border)

        return ((angle >= leftBorder) && (angle <= rightBorder))
    }

    private fun getAbsoluteAngle(
        posX: Float,
        posY: Float,
    ): Float {
        val dx = posX - x
        val dy = posY - y

        return normalizeOrientation(atan2(dy, dx))
    }

    private fun getRelativeAngle(position: Position): Float = toRelativeAngle(getAbsoluteAngle(position.x, position.y))

    private fun toRelativeAngle(absAngle: Float) = normalizeOrientation(absAngle - orientation)

    private fun normalizeOrientation(orientation: Float): Float {
        if (orientation < 0) {
            var mod = orientation * -1
            mod %= ANGLE_2_PI
            mod = -mod + ANGLE_2_PI

            return mod
        }

        return orientation % ANGLE_2_PI
    }

    override fun toString(): String =
        """
        {
            x: $x,
            y: $y,
            z: $z,
            orientation: $orientation
        }
        """.trimIndent()

    override fun hashCode(): Int {
        var result = x.hashCode()
        result = 31 * result + y.hashCode()
        result = 31 * result + z.hashCode()
        result = 31 * result + orientation.hashCode()
        return result
    }

    override fun equals(other: Any?): Boolean = other is Position && other.isSameHas(this)

    companion object {
        private const val ANGLE_PI = PI.toFloat()
        const val ANGLE_2_PI = (2.0 * ANGLE_PI).toFloat()
        const val ANGLE_2_PI_3 = ANGLE_2_PI / 3
    }
}
