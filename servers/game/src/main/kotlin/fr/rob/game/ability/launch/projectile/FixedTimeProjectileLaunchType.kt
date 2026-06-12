package fr.rob.game.ability.launch.projectile

import fr.rob.game.ability.Ability
import fr.rob.game.ability.projectile.CarryProjectileInterface
import fr.rob.game.ability.projectile.TimedProjectile

class FixedTimeProjectileLaunchType : AbstractProjectileLaunchType() {
    override fun createProjectile(ability: Ability): CarryProjectileInterface {
        // Add context to exception (ability id for instance)
        val parameters = ability.info.launchType.parameters
            ?: throw IllegalStateException("Parameters must be set for projectile launch type (ability identifier: ${ability.info.identifier})")

        if (parameters !is FixedTimeProjectileParameters) {
            throw IllegalStateException("Parameters must be of type ${FixedTimeProjectileParameters::class.java.simpleName}")
        }

        return TimedProjectile(parameters.travelTimeMs)
    }
}
