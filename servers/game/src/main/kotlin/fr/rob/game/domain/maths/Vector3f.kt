package fr.rob.game.domain.maths

import kotlin.math.sqrt

class Vector3f(var x: Float, var y: Float, var z: Float) {
    fun magnitude(): Float = sqrt(x * x + y * y + z * z)

    fun normalize(): Vector3f {
        val magnitude = magnitude()

        return Vector3f(
            x * (1 / magnitude),
            y * (1 / magnitude),
            z * (1 / magnitude),
        )
    }
}
