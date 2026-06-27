package fr.rob.game.ability

import fr.rob.game.entity.WorldObject
import fr.rob.core.misc.clock.IntervalTimer
import fr.rob.game.ability.event.AbilityStateUpdateEvent
import fr.rob.game.ability.launch.LaunchTypeInterface

class Ability(
    val entryId: Int,
    val info: AbilityInfo,
    val source: WorldObject,
    val target: AbilityTargetParameter,
) {
    var launchType: LaunchTypeInterface? = null
    private var _state: AbilityState = AbilityState.NOT_STARTED
    val state: AbilityState get() = _state

    fun setState(newState: AbilityState) {
        val oldState = _state
        _state = newState
        source.pushEvent(AbilityStateUpdateEvent(oldState, _state, this))
    }

    private val castingTimer = IntervalTimer(info.castingTimeMs)

    fun isCastCompleted(): Boolean = castingTimer.passed()

    fun isDone(): Boolean = state == AbilityState.DONE || failed()

    fun isInProgress(): Boolean = !isDone()

    fun failed(): Boolean = state == AbilityState.FAILED

    fun getRemainingCastingTimeMs(): Int = if (isCastCompleted()) 0 else castingTimer.localTime

    fun updateTimers(deltaTime: Int) {
        castingTimer.update(deltaTime)
    }

    enum class AbilityState(val value: Int) {
        NOT_STARTED(0),
        CASTING(1),
        LAUNCHING(2),
        RESOLVING(3),
        DONE(4),
        FAILED(5),
    }
}
