package fr.rob.game.ability

import fr.rob.game.entity.WorldObject
import fr.rob.core.misc.clock.IntervalTimer
import fr.rob.game.ability.state.AbilityFlow
import fr.rob.game.ability.state.AbilityState

class Ability(
    val info: AbilityInfo,
    val source: WorldObject,
    private val target: AbilityTargetParameter,
) {
    private val abilityLauncher = info.launchInfo.createAbilityLauncher(this)
    private val flow = AbilityFlow(this, abilityLauncher)
    private val castingTimer = IntervalTimer(info.castingTimeMs)

    fun use() {
        flow.useAbility()
    }

    fun resume(elapsedTimeMs: Int) {
        if (flow.getCurrentState() == AbilityState.PerformingAbility) {
            castingTimer.update(elapsedTimeMs)

            flow.resumeCasting(castingTimer.passed())
        }

        if (flow.getCurrentState() == AbilityState.Launching) {
            flow.resumeLaunching()
        }
    }

    fun isDone(): Boolean = flow.getCurrentState() == AbilityState.Done || failed()

    fun isInProgress(): Boolean = !isDone()

    fun failed(): Boolean = flow.getCurrentState() == AbilityState.Failed
}
