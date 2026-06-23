package fr.rob.game.ability.launch.projectile

import fr.rob.game.ability.Ability
import fr.rob.game.ability.projectile.CarryProjectileInterface
import fr.rob.game.ability.projectile.TimedProjectile

class FixedTimeProjectileLaunchType(
    private val parameters: FixedTimeProjectileParameters,
) : AbstractProjectileLaunchType() {
    override fun createProjectile(ability: Ability): CarryProjectileInterface =
        TimedProjectile(parameters.travelTimeMs)
}
