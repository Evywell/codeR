package fr.rob.game.ability.trigger

import fr.rob.game.ability.Ability
import fr.rob.game.ability.effect.AbilityEffectInfoInterface
import fr.rob.game.ability.effect.AbilityEffectSummary

class ApplyAbilityEffectsTrigger(private val effects: Array<AbilityEffectInfoInterface>) : AbilityTriggerInterface {
    override fun trigger(ability: Ability) {
        val effectSummary = AbilityEffectSummary()

        effects.forEach {
            applyEffect(ability, it, effectSummary)
        }
    }

    private fun applyEffect(ability: Ability, effectInfo: AbilityEffectInfoInterface, effectSummary: AbilityEffectSummary) {
        effectInfo
            .createEffectFromAbility(ability)
            .apply(effectSummary)
    }
}