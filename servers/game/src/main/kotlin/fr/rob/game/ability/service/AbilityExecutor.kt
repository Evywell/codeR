package fr.rob.game.ability.service

import fr.rob.game.ability.Ability
import fr.rob.game.ability.service.phase.AbilityPhaseHandlerInterface

class AbilityExecutor(
    private val requirementChecker: AbilityRequirementChecker,
    private val phaseHandlers: List<AbilityPhaseHandlerInterface>,
) {

    fun startAbility(ability: Ability) {
        if (ability.state != Ability.AbilityState.NOT_STARTED) {
            return
        }

        if (!requirementChecker.checkRequirements(ability)) {
            return
        }

        ability.state = Ability.AbilityState.CASTING
        processAbility(ability, 0)
    }

    fun updateAbility(ability: Ability, elapsedTimeMs: Int) {
        if (ability.isDone()) {
            return
        }

        ability.updateTimers(elapsedTimeMs)
        processAbility(ability, elapsedTimeMs)
    }

    private fun processAbility(ability: Ability, deltaTime: Int) {
        for (handler in phaseHandlers) {
            if (handler.canHandle(ability)) {
                handler.handle(ability, deltaTime)
            }
        }
    }
}
