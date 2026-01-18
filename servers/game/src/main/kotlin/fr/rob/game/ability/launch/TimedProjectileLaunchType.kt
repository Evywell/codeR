package fr.rob.game.ability.launch

import fr.rob.game.ability.Ability
import fr.rob.game.ability.projectile.CarryProjectileInterface
import fr.rob.game.ability.projectile.TimedProjectile

class TimedProjectileLaunchType(
    private val launchInfo: TimedProjectileLaunchInfo,
) : AbstractProjectileLaunchType() {
    override fun onHitTarget(ability: Ability) {
        launchInfo.onHitTargetTrigger.trigger(ability)
    }

    override fun createProjectile(ability: Ability): CarryProjectileInterface =
        TimedProjectile(launchInfo.travelTimeMs)
}
