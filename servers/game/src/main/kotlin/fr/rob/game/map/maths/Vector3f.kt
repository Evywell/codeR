package fr.rob.game.map.maths

import fr.raven.proto.message.physicbridge.PhysicProto.Vec3
import kotlin.math.sqrt

class Vector3f(var x: Float, var y: Float, var z: Float) {
    fun magnitude(): Float = sqrt(x * x + y * y + z * z)

    fun normalize(): Vector3f {
        val magnitude = magnitude()

        if (magnitude <= 0) {
            return Vector3f(0f, 0f, 0f)
        }

        return Vector3f(
            x * (1 / magnitude),
            y * (1 / magnitude),
            z * (1 / magnitude),
        )
    }

    companion object {
        fun zero(): Vector3f = Vector3f(0f, 0f, 0f)
        fun forward(): Vector3f = Vector3f(0f, 0f, 1f)

        fun fromVec3Message(message: Vec3): Vector3f = Vector3f(message.x, message.y, message.z)
    }
}
