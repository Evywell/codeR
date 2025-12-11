package fr.rob.game.spell.projectile

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

        val normalizedVector = vectorBetweenPositions.normalize()
        val traveledDistance = speed * (deltaTime / 1000f)

        position.x += normalizedVector.x * traveledDistance
        position.y += normalizedVector.y * traveledDistance
        position.z += normalizedVector.z * traveledDistance

        hasHitTarget = position.isSameHas(target.position)
    }
}
