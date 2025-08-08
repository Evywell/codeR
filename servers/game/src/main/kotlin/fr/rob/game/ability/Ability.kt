package fr.rob.game.ability

import fr.rob.game.entity.WorldObject
import fr.rob.core.misc.clock.IntervalTimer
import fr.rob.game.ability.state.AbilityFlow
import fr.rob.game.ability.state.AbilityState

class Ability(
    private val info: AbilityInfo,
    private val source: WorldObject,
    private val target: AbilityTargetParameter,
) {
    private val abilityLauncher = info.launchInfo.createAbilityLauncher()
    private val flow = AbilityFlow(source, info)
    private val castingTimer = IntervalTimer(info.castingTimeMs)

    fun use() {
        flow.useAbility()
    }

    fun resume(elapsedTimeMs: Int) {
        if (flow.getCurrentState() == AbilityState.PerformingAbility) {
            castingTimer.update(elapsedTimeMs)

            flow.resumeCasting(castingTimer.passed())
        }
    }

    fun isDone(): Boolean = flow.getCurrentState() == AbilityState.Done || failed()

    fun isInProgress(): Boolean = !isDone()

    fun failed(): Boolean = flow.getCurrentState() == AbilityState.Failed
}
