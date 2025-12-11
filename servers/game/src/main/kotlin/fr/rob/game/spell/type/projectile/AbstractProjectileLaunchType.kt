package fr.rob.game.spell.type.projectile

import fr.rob.game.spell.Spell
import fr.rob.game.spell.projectile.CarryProjectileInterface
import fr.rob.game.spell.type.LaunchTypeInterface
import fr.rob.game.spell.type.UpdatableLaunchInterface

abstract class AbstractProjectileLaunchType : LaunchTypeInterface, UpdatableLaunchInterface {
    private lateinit var projectile: CarryProjectileInterface

    override fun handleLaunch(spell: Spell) {
        projectile = createProjectile(spell)
    }

    override fun getCurrentState(): Spell.SpellState {
        if (projectile.hasHitTarget()) {
            return Spell.SpellState.EFFECT_APPLIED
        }

        return Spell.SpellState.ONGOING
    }

    override fun update(deltaTime: Int, spell: Spell) {
        projectile.update(deltaTime)

        if (projectile.hasHitTarget()) {
            onHitTarget(spell)
        }
    }

    protected abstract fun onHitTarget(spell: Spell)
    protected abstract fun createProjectile(spell: Spell): CarryProjectileInterface
}
