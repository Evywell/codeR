package fr.rob.game.ability.service

import fr.rob.game.ability.Ability
import fr.rob.game.ability.AbilityInfo
import fr.rob.game.ability.event.AbilityFailedEvent
import fr.rob.game.entity.WorldObject

class AbilityRequirementChecker {

    fun checkRequirements(ability: Ability): Boolean {
        return checkResourceRequirements(ability)
    }

    fun checkCastingRequirements(ability: Ability): Boolean {
        return checkMovementRequirement(ability)
    }

    private fun checkResourceRequirements(ability: Ability): Boolean {
        for (resource in ability.info.abilityRequirement.resources) {
            if (!resource.hasEnoughResources(ability.source)) {
                val resourceType = resource::class.simpleName ?: "unknown"
                failAbility(ability, "Not enough $resourceType")
                return false
            }
        }
        return true
    }

    private fun checkMovementRequirement(ability: Ability): Boolean {
        if (!ability.info.flags.contains(AbilityInfo.FLAGS.ALLOW_CAST_WHILE_MOVING)) {
            if (isMoving(ability.source)) {
                failAbility(ability, "Movement interrupted casting")
                return false
            }
        }
        return true
    }

    private fun isMoving(source: WorldObject): Boolean {
        val movementComponent = source.getComponent<fr.rob.game.component.MovementComponent>()
        return movementComponent?.isMoving() == true
    }

    private fun failAbility(ability: Ability, reason: String) {
        ability.state = Ability.AbilityState.FAILED
        ability.source.pushEvent(AbilityFailedEvent(ability, reason))
    }
}
