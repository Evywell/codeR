package fr.rob.game.domain.spell.type.projectile

import fr.rob.game.domain.entity.Position
import fr.rob.game.domain.spell.Spell
import fr.rob.game.domain.spell.projectile.CarryProjectileInterface
import fr.rob.game.domain.spell.projectile.TimedProjectile

class TimedProjectileLaunchType(private val launchInfo: TimedProjectileLaunchInfo) : AbstractProjectileLaunchType() {
    override fun onHitTarget(spell: Spell) {
        launchInfo.onHitTargetTrigger.trigger(spell)
    }

    override fun createProjectile(spell: Spell): CarryProjectileInterface {
        val caster = spell.caster
        val target = spell.target.getPrimaryTarget().get()

        return TimedProjectile(
            Position(caster.position.x, caster.position.y, caster.position.z, caster.position.orientation),
            target,
            launchInfo.projectileSpeed
        )
    }
}
