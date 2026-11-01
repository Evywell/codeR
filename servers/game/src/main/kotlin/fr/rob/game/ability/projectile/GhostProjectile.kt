package fr.rob.game.ability.projectile

import fr.rob.game.entity.Position
import fr.rob.game.entity.guid.ObjectGuid
import fr.rob.game.instance.MapInstance
import fr.rob.game.map.maths.Vector3f

class GhostProjectile(
    private val position: Position,
    private val targetGuid: ObjectGuid,
    private val mapInstance: MapInstance,
    private val speed: Float,
) : CarryProjectileInterface {
    private var hasHitTarget = false

    // When the target leaves the instance, the projectile keeps flying to where it was last seen.
    private var lastKnownTargetPosition: Position? = null

    override fun hasHitTarget(): Boolean = hasHitTarget

    override fun update(deltaTime: Int) {
        if (hasHitTarget) {
            return
        }

        mapInstance.findObjectByGuid(targetGuid)?.position?.let {
            lastKnownTargetPosition = Position(it.x, it.y, it.z, it.orientation)
        }

        val target = lastKnownTargetPosition ?: return

        val vectorBetweenPositions = Vector3f(
            target.x - position.x,
            target.y - position.y,
            target.z - position.z,
        )

        val distance = vectorBetweenPositions.magnitude()
        val traveledDistance = speed * (deltaTime / 1000f)

        // Check if we've reached or passed the target
        if (traveledDistance >= distance) {
            position.x = target.x
            position.y = target.y
            position.z = target.z
            hasHitTarget = true
            return
        }

        val normalizedVector = vectorBetweenPositions.normalize()

        position.x += normalizedVector.x * traveledDistance
        position.y += normalizedVector.y * traveledDistance
        position.z += normalizedVector.z * traveledDistance
    }
}
