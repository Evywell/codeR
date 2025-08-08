package fr.rob.game.ability

import fr.rob.game.ability.exception.AbilityNotConfiguredCorrectlyException
import fr.rob.game.ability.exception.ObjectCannotUseAbilityException
import fr.rob.game.ability.exception.UnknownAbilityException
import fr.rob.game.entity.WorldObject
import java.util.Optional

class ObjectAbilityManager {
    private val abilityInfo = HashMap<Int, AbilityInfo>()

    fun useAbilityFromIdentifier(
        source: WorldObject,
        abilityId: Int,
        targetParameter: AbilityTargetParameter,
    ) {
        val abilityInfoQuery = tryGetAbilityInfo(abilityId)

        if (abilityInfoQuery.isEmpty) {
            throw UnknownAbilityException()
        }

        val abilityInfo = abilityInfoQuery.get()

        if (abilityId != abilityInfo.identifier) {
            throw AbilityNotConfiguredCorrectlyException(
                "Ability id mismatch: $abilityId given but ${abilityInfo.identifier} configured in the ability info",
            )
        }

        if (!source.hasAbility(abilityInfo)) {
            throw ObjectCannotUseAbilityException("Object ${source.guid.getRawValue()} cannot use ability $abilityId")
        }

        val ability = Ability(abilityInfo, source, targetParameter)

        source.performAbility(ability)
    }

    private fun tryGetAbilityInfo(abilityId: Int): Optional<AbilityInfo> = Optional.ofNullable(abilityInfo[abilityId])
}
