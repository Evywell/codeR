package fr.rob.game.spell.type.projectile

import fr.rob.game.entity.Position
import fr.rob.game.spell.Spell
import fr.rob.game.spell.projectile.CarryProjectileInterface
import fr.rob.game.spell.projectile.GhostProjectile

class GhostProjectileLaunchType(private val launchInfo: GhostProjectileLaunchInfo) : AbstractProjectileLaunchType() {
    override fun onHitTarget(spell: Spell) {
        launchInfo.onHitTargetTrigger.trigger(spell)
    }

    override fun createProjectile(spell: Spell): CarryProjectileInterface {
        val caster = spell.caster
        val target = spell.target.getPrimaryTarget().get()

        return GhostProjectile(
            Position(caster.position.x, caster.position.y, caster.position.z, caster.position.orientation),
            target,
            launchInfo.projectileSpeed
        )
    }
}
