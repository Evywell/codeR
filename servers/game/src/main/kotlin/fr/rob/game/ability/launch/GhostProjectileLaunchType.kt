package fr.rob.game.ability.launch

import fr.rob.game.ability.Ability
import fr.rob.game.ability.projectile.CarryProjectileInterface
import fr.rob.game.ability.projectile.GhostProjectile
import fr.rob.game.entity.Position

class GhostProjectileLaunchType(
    private val launchInfo: GhostProjectileLaunchInfo,
) : AbstractProjectileLaunchType() {
    override fun createProjectile(ability: Ability): CarryProjectileInterface {
        val caster = ability.source
        val target = ability.target.getPrimaryTarget()
            ?: throw IllegalStateException("Cannot create projectile without a target")

        return GhostProjectile(
            Position(caster.position.x, caster.position.y, caster.position.z, caster.position.orientation),
            target,
            launchInfo.projectileSpeed,
        )
    }
}
