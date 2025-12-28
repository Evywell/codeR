package fr.rob.game.spell

import fr.rob.core.misc.clock.IntervalTimer
import fr.rob.game.component.MovementComponent
import fr.rob.game.entity.WorldObject
import fr.rob.game.spell.target.SpellTargetParameter
import fr.rob.game.spell.type.UpdatableLaunchInterface

class Spell(
    val spellInfo: SpellInfo,
    val caster: WorldObject,
    val target: SpellTargetParameter,
) {
    var state = SpellState.PREPARING
        private set

    private val spellLauncher = spellInfo.launchInfo.createSpellLauncher()
    private val castingTimer = IntervalTimer(spellInfo.castingTime)

    fun cast() {
        update(0)
    }

    fun update(deltaTime: Int) {
        updateTimers(deltaTime)
        handleNextStep(deltaTime)
    }

    fun isEnded(): Boolean = state == SpellState.ENDED || state == SpellState.CANCELED

    private fun handleNextStep(deltaTime: Int) {
        when (state) {
            SpellState.PREPARING -> handlePreparingPhase(deltaTime)
            SpellState.LAUNCHING -> handleLaunchingPhase(deltaTime)
            SpellState.ENDED -> {}
            SpellState.CANCELED -> {}
            SpellState.ONGOING -> handleOngoingPhase(deltaTime)
            SpellState.EFFECT_APPLIED -> handleEffectAppliedPhase(deltaTime)
        }
    }

    private fun handleEffectAppliedPhase(deltaTime: Int) {
        state = SpellState.ENDED
        handleNextStep(deltaTime)
    }

    private fun handlePreparingPhase(deltaTime: Int) {
        if (
            spellInfo.castingTime > SpellInfo.INSTANT_CASTING_TIME &&
            isCastSequenceOngoing()
        ) {
            checkCastingSequenceRequirements()

            return
        }

        state = SpellState.LAUNCHING
        handleNextStep(deltaTime)
    }

    private fun checkCastingSequenceRequirements() {
        // Movement should cancel the cast unless it is authorized
        if (!spellInfo.flags.contains(SpellInfo.FLAGS.ALLOW_CAST_WHILE_MOVING)) {
            if (caster.getComponent<MovementComponent>()?.isMoving() == true) {
                state = SpellState.CANCELED

                return
            }
        }
    }

    private fun isCastSequenceOngoing(): Boolean = !castingTimer.passed()

    private fun handleLaunchingPhase(deltaTime: Int) {
        spellLauncher.handleLaunch(this)

        state = SpellState.ONGOING
        handleNextStep(deltaTime)
    }

    private fun handleOngoingPhase(deltaTime: Int) {
        if (spellLauncher is UpdatableLaunchInterface) {
            spellLauncher.update(deltaTime, this)
        }

        val nextState = spellLauncher.getCurrentState()

        if (nextState == state) {
            return
        }

        state = nextState

        handleNextStep(deltaTime)
    }

    private fun updateTimers(deltaTime: Int) {
        castingTimer.update(deltaTime)
    }

    enum class SpellState {
        PREPARING,
        LAUNCHING,
        ONGOING,
        EFFECT_APPLIED,
        ENDED,
        CANCELED,
    }
}
