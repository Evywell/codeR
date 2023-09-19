package fr.rob.game.domain.spell.projectile

import fr.rob.core.misc.clock.IntervalTimer
import fr.rob.game.domain.entity.Position
import fr.rob.game.domain.entity.WorldObject
import kotlin.math.floor
import kotlin.math.sqrt

class TimedProjectile(
    val position: Position,
    val target: WorldObject,
    val speed: Float,
) : CarryProjectileInterface {
    private val timer: IntervalTimer
    private var hasHitTarget = false

    init {
        val distanceToTravel = sqrt(
            (target.position.x - position.x) * (target.position.x - position.x) +
                (target.position.y - position.y) * (target.position.y - position.y) +
                (target.position.z - position.z) * (target.position.z - position.z),
        )

        timer = IntervalTimer(floor(distanceToTravel / speed * 1000).toInt())
    }

    override fun update(deltaTime: Int) {
        if (hasHitTarget) {
            return
        }

        timer.update(deltaTime)

        hasHitTarget = timer.passed()
    }

    override fun hasHitTarget(): Boolean = hasHitTarget
}
