package fr.rob.game.domain.spell

import fr.rob.game.domain.entity.Position
import fr.rob.game.domain.entity.Unit
import fr.rob.game.domain.entity.WorldObject
import fr.rob.game.domain.spell.effect.InstantDamageEffect
import fr.rob.game.domain.spell.effect.SpellEffectInfo
import fr.rob.game.domain.spell.effect.SpellEffectTypeEnum
import fr.rob.game.domain.spell.projectile.CarryProjectileInterface
import fr.rob.game.domain.spell.projectile.GhostProjectile
import fr.rob.game.domain.spell.projectile.TimedProjectile
import fr.rob.game.domain.spell.target.SpellTargetParameter

class Spell(
    private val spellInfo: SpellInfo,
    private val caster: WorldObject,
    private val target: SpellTargetParameter,
) {
    private var state = SpellState.PREPARING
    private var projectile: CarryProjectileInterface? = null
    private var lastUpdateDeltaTime: Int = 0

    fun cast() {
        handleNextStep()
    }

    fun update(deltaTime: Int) {
        updateTimers(deltaTime)
        cast()
    }

    fun isEnded(): Boolean = state == SpellState.ENDED

    private fun handleNextStep() {
        when (state) {
            SpellState.PREPARING -> handlePreparingPhase()
            SpellState.LAUNCHING -> handleLaunchingPhase()
            SpellState.PROJECTILE_TRAVELING -> handleProjectileTravelingPhase()
            SpellState.APPLY_EFFECTS -> handleApplyEffectsPhase()
            SpellState.ENDED -> {}
        }
    }

    private fun handlePreparingPhase() {
        if (spellInfo.launchingType == SpellInfo.LaunchType.INSTANT) {
            state = SpellState.APPLY_EFFECTS
            handleNextStep()

            return
        }

        state = SpellState.LAUNCHING
        handleNextStep()
    }

    private fun handleLaunchingPhase() {
        if (spellInfo.launchingType == SpellInfo.LaunchType.INSTANT) {
            state = SpellState.APPLY_EFFECTS
            handleNextStep()

            return
        }

        if (target.getPrimaryTarget().isEmpty) {
            // @todo handle the unexpected situation correctly
            state = SpellState.ENDED

            return
        }

        createProjectile()

        state = SpellState.PROJECTILE_TRAVELING
        handleNextStep()
    }

    private fun createProjectile() {
        when (spellInfo.launchingType) {
            SpellInfo.LaunchType.INSTANT -> {}
            SpellInfo.LaunchType.GHOST_PROJECTILES -> createGhostProjectile(target.getPrimaryTarget().get())
            SpellInfo.LaunchType.TIMED_PROJECTILES -> createTimedProjectile(target.getPrimaryTarget().get())
        }
    }

    private fun handleProjectileTravelingPhase() {
        projectile?.update(lastUpdateDeltaTime)

        projectile?.let {
            if (it.hasHitTarget()) {
                state = SpellState.APPLY_EFFECTS
                handleNextStep()
            }
        }
    }

    private fun handleApplyEffectsPhase() {
        spellInfo.effects.forEach {
            applyEffect(it)
        }

        state = SpellState.ENDED
        handleNextStep()
    }

    private fun createTimedProjectile(target: WorldObject) {
        projectile = TimedProjectile(
            Position(caster.position.x, caster.position.y, caster.position.z, caster.position.orientation),
            target,
            spellInfo.projectileSpeed,
        )
    }

    private fun createGhostProjectile(target: WorldObject) {
        projectile = GhostProjectile(
            Position(caster.position.x, caster.position.y, caster.position.z, caster.position.orientation),
            target,
            spellInfo.projectileSpeed,
        )
    }

    private fun applyEffect(effectInfo: SpellEffectInfo) {
        val effect = when (effectInfo.type) {
            SpellEffectTypeEnum.INSTANT_DAMAGE -> InstantDamageEffect(effectInfo as InstantDamageEffect.InstantDamageEffectInfo, target, (caster as Unit).level)
        }

        effect.cast()
    }

    private fun updateTimers(deltaTime: Int) {
        lastUpdateDeltaTime = deltaTime
    }

    private enum class SpellState {
        PREPARING,
        LAUNCHING,
        PROJECTILE_TRAVELING,
        APPLY_EFFECTS,
        ENDED,
    }
}
