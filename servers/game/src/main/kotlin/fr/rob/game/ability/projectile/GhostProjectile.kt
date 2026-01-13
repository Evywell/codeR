package fr.rob.game.ability.projectile

import fr.rob.game.entity.Position
import fr.rob.game.entity.WorldObject
import fr.rob.game.map.maths.Vector3f

class GhostProjectile(
    private val position: Position,
    private val target: WorldObject,
    private val speed: Float,
) : CarryProjectileInterface {
    private var hasHitTarget = false

    override fun hasHitTarget(): Boolean = hasHitTarget

    override fun update(deltaTime: Int) {
        if (hasHitTarget) {
            return
        }

        val vectorBetweenPositions = Vector3f(
            target.position.x - position.x,
            target.position.y - position.y,
            target.position.z - position.z,
        )

        val distance = vectorBetweenPositions.magnitude()
        val traveledDistance = speed * (deltaTime / 1000f)

        // Check if we've reached or passed the target
        if (traveledDistance >= distance) {
            position.x = target.position.x
            position.y = target.position.y
            position.z = target.position.z
            hasHitTarget = true
            return
        }

        val normalizedVector = vectorBetweenPositions.normalize()

        position.x += normalizedVector.x * traveledDistance
        position.y += normalizedVector.y * traveledDistance
        position.z += normalizedVector.z * traveledDistance
    }
}
