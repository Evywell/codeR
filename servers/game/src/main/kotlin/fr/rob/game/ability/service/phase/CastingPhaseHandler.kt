package fr.rob.game.ability.service.phase

import fr.rob.game.ability.Ability
import fr.rob.game.ability.AbilityInfo
import fr.rob.game.ability.service.AbilityRequirementChecker

class CastingPhaseHandler(
    private val requirementChecker: AbilityRequirementChecker,
) : AbilityPhaseHandlerInterface {

    override fun canHandle(ability: Ability): Boolean =
        ability.state == Ability.AbilityState.CASTING

    override fun handle(ability: Ability, deltaTime: Int) {
        if (ability.isCastCompleted()) {

            consumeResources(ability)
            ability.state = Ability.AbilityState.LAUNCHING

            return
        }

        requirementChecker.checkCastingRequirements(ability)
    }

    private fun consumeResources(ability: Ability) {
        for (resource in ability.info.abilityRequirement.resources) {
            resource.computeResources(ability.source)
        }
    }
}
