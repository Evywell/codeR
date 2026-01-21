package fr.rob.game.ability.effect

import fr.rob.game.ability.Ability

interface EffectFromAbilityInterface {
    fun createEffectFromAbility(ability: Ability): EffectInterface
}