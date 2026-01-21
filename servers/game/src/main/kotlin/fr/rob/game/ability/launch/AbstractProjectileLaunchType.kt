package fr.rob.game.ability.launch

import fr.rob.game.ability.Ability
import fr.rob.game.ability.projectile.CarryProjectileInterface

abstract class AbstractProjectileLaunchType : UpdatableLaunchTypeInterface {
    private var projectile: CarryProjectileInterface? = null

    override fun handleLaunch() {}

    override fun isLaunchingCompleted(): Boolean {
        return projectile?.hasHitTarget() ?: false
    }

    override fun update(deltaTime: Int, ability: Ability) {
        if (projectile == null) {
            projectile = createProjectile(ability)
        }

        projectile?.update(deltaTime)
    }

    protected abstract fun createProjectile(ability: Ability): CarryProjectileInterface
}
