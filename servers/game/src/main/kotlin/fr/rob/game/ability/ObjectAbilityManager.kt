package fr.rob.game.ability

import fr.rob.game.ability.exception.AbilityNotConfiguredCorrectlyException
import fr.rob.game.ability.exception.ObjectCannotUseAbilityException
import fr.rob.game.ability.exception.UnknownAbilityException
import fr.rob.game.ability.service.AbilityExecutor
import fr.rob.game.entity.WorldObject

class ObjectAbilityManager(
    private val abilityExecutor: AbilityExecutor,
) {
    private val abilityInfo = HashMap<Int, AbilityInfo>()

    fun defineAbility(info: AbilityInfo) {
        abilityInfo[info.identifier] = info
    }

    fun useAbilityFromIdentifier(
        source: WorldObject,
        abilityId: Int,
        targetParameter: AbilityTargetParameter,
    ) {
        val abilityInfo = abilityInfo[abilityId] ?: throw UnknownAbilityException()

        if (abilityId != abilityInfo.identifier) {
            throw AbilityNotConfiguredCorrectlyException(
                "Ability id mismatch: $abilityId given but ${abilityInfo.identifier} configured in the ability info",
            )
        }

        if (!source.hasAbility(abilityInfo)) {
            throw ObjectCannotUseAbilityException("Object ${source.guid.getRawValue()} cannot use ability $abilityId")
        }

        val ability = Ability(abilityInfo, source, targetParameter)

        abilityExecutor.startAbility(ability)

        if (ability.isInProgress()) {
            source.addOngoingAbility(ability)
        }
    }

    fun updateAbility(ability: Ability, deltaTime: Int) {
        abilityExecutor.updateAbility(ability, deltaTime)
    }
}
