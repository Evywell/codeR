package fr.rob.game.domain.spell

import fr.rob.core.misc.clock.IntervalTimer
import fr.rob.game.domain.entity.Position
import fr.rob.game.domain.entity.WorldObject
import fr.rob.game.domain.entity.behavior.MovableTrait
import fr.rob.game.domain.entity.behavior.ObjectSheetTrait
import fr.rob.game.domain.spell.effect.EffectFromSpellInterface
import fr.rob.game.domain.spell.projectile.CarryProjectileInterface
import fr.rob.game.domain.spell.projectile.GhostProjectile
import fr.rob.game.domain.spell.projectile.TimedProjectile
import fr.rob.game.domain.spell.target.SpellTargetParameter

class Spell(
    private val spellInfo: SpellInfo,
    val caster: WorldObject,
    val target: SpellTargetParameter,
) {
    var state = SpellState.PREPARING
        private set

    private val castingTimer = IntervalTimer(spellInfo.castingTime)
    private var projectile: CarryProjectileInterface? = null
    private var lastUpdateDeltaTime: Int = 0

    fun cast() {
        handleNextStep()
    }

    fun update(deltaTime: Int) {
        updateTimers(deltaTime)
        cast()
    }

    fun isEnded(): Boolean = state == SpellState.ENDED || state == SpellState.CANCELED

    private fun handleNextStep() {
        when (state) {
            SpellState.PREPARING -> handlePreparingPhase()
            SpellState.LAUNCHING -> handleLaunchingPhase()
            SpellState.PROJECTILE_TRAVELING -> handleProjectileTravelingPhase()
            SpellState.APPLY_EFFECTS -> handleApplyEffectsPhase()
            SpellState.ENDED -> {}
            SpellState.CANCELED -> {}
        }
    }

    private fun handlePreparingPhase() {
        if (
            spellInfo.castingTime > SpellInfo.INSTANT_CASTING_TIME &&
            isCastSequenceOngoing()
        ) {
            checkCastingSequenceRequirements()

            return
        }

        if (spellInfo.launchingType == SpellInfo.LaunchType.INSTANT) {
            state = SpellState.APPLY_EFFECTS
            handleNextStep()

            return
        }

        state = SpellState.LAUNCHING
        handleNextStep()
    }

    private fun checkCastingSequenceRequirements() {
        // Movement should cancel the cast unless it is authorized
        if (!spellInfo.flags.contains(SpellInfo.FLAGS.ALLOW_CAST_WHILE_MOVING)) {
            val movableCaster = caster.getTrait<MovableTrait>()

            if (movableCaster.isPresent && movableCaster.get().isMoving()) {
                state = SpellState.CANCELED

                return
            }
        }
    }

    private fun isCastSequenceOngoing(): Boolean = !castingTimer.passed()

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
            SpellInfo.LaunchType.INSTANT -> {} // For the moment, there is no projectile when the spell is instant
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
        val spellEffectSummary = SpellEffectSummary()

        spellInfo.effects.forEach {
            applyEffect(it, spellEffectSummary)
        }

        handleSpellDamages(spellEffectSummary)

        state = SpellState.ENDED
        handleNextStep()
    }

    private fun handleSpellDamages(spellEffectSummary: SpellEffectSummary) {
        spellEffectSummary.forEach { target, sources ->
            // @todo hit check

            target.getTrait(ObjectSheetTrait::class).ifPresent {
                val isCritical = it.isCriticalHit(caster)

                it.applyDamages(sources, isCritical)
            }
        }
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

    private fun applyEffect(effectInfo: SpellEffectInfo, spellEffectSummary: SpellEffectSummary) {
        if (effectInfo is EffectFromSpellInterface) {
            effectInfo.createEffectFromSpell(this).cast(spellEffectSummary)
        }
    }

    private fun updateTimers(deltaTime: Int) {
        lastUpdateDeltaTime = deltaTime
        castingTimer.update(deltaTime)
    }

    enum class SpellState {
        PREPARING,
        LAUNCHING,
        PROJECTILE_TRAVELING,
        APPLY_EFFECTS,
        ENDED,
        CANCELED,
    }
}
