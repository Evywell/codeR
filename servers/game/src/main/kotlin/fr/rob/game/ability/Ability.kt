package fr.rob.game.ability

import fr.rob.game.entity.WorldObject
import fr.rob.core.misc.clock.IntervalTimer
import fr.rob.game.ability.launch.LaunchTypeInterface

class Ability(
    val info: AbilityInfo,
    val source: WorldObject,
    val target: AbilityTargetParameter,
) {
    var state = AbilityState.NOT_STARTED
    var launchType: LaunchTypeInterface? = null

    private val castingTimer = IntervalTimer(info.castingTimeMs)

    fun isCastCompleted(): Boolean = castingTimer.passed()

    fun isDone(): Boolean = state == AbilityState.DONE || failed()

    fun isInProgress(): Boolean = !isDone()

    fun failed(): Boolean = state == AbilityState.FAILED

    fun updateTimers(deltaTime: Int) {
        castingTimer.update(deltaTime)
    }

    enum class AbilityState {
        NOT_STARTED,
        CASTING,
        LAUNCHING,
        RESOLVING,
        DONE,
        FAILED,
    }
}
