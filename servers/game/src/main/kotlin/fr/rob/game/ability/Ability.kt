package fr.rob.game.ability

import fr.rob.game.ability.launch.LaunchTypeInterface
import fr.rob.game.entity.WorldObject
import fr.rob.core.misc.clock.IntervalTimer

class Ability(
    val info: AbilityInfo,
    val source: WorldObject,
    val target: AbilityTargetParameter,
) {
    var state = AbilityState.NOT_STARTED

    private val abilityLauncher: LaunchTypeInterface = info.launchInfo.createAbilityLauncher(this)
    private val castingTimer = IntervalTimer(info.castingTimeMs)

    fun isCastCompleted(): Boolean = castingTimer.passed()

    fun isDone(): Boolean = state == AbilityState.DONE || failed()

    fun isInProgress(): Boolean = !isDone()

    fun failed(): Boolean = state == AbilityState.FAILED

    fun getLauncher(): LaunchTypeInterface = abilityLauncher

    fun updateTimers(deltaTime: Int) {
        castingTimer.update(deltaTime)
    }

    enum class AbilityState {
        NOT_STARTED,
        CASTING,
        RESOLVING,
        DONE,
        FAILED,
    }
}
