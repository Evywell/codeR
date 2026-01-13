package fr.rob.game.ability.projectile

import fr.rob.core.misc.clock.IntervalTimer

class TimedProjectile(
    travelTimeMs: Int,
) : CarryProjectileInterface {
    private val timer = IntervalTimer(travelTimeMs)

    override fun hasHitTarget(): Boolean = timer.passed()

    override fun update(deltaTime: Int) {
        timer.update(deltaTime)
    }
}
