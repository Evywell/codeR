package fr.rob.game.ability.trigger

import fr.rob.game.ability.Ability
import fr.rob.game.ability.effect.AbilityEffectInfoInterface
import fr.rob.game.ability.effect.AbilityEffectSummary
import fr.rob.game.behavior.ObjectSheetBehavior

class ApplyAbilityEffectsTrigger(private val effects: Array<AbilityEffectInfoInterface>) : AbilityTriggerInterface {
    override fun trigger(ability: Ability) {
        val effectSummary = AbilityEffectSummary()

        effects.forEach {
            applyEffect(ability, it, effectSummary)
        }

        handleDamages(effectSummary, ability)
    }

    private fun applyEffect(ability: Ability, effectInfo: AbilityEffectInfoInterface, effectSummary: AbilityEffectSummary) {
        effectInfo
            .createEffectFromAbility(ability)
            .apply(effectSummary)
    }

    private fun handleDamages(effectSummary: AbilityEffectSummary, ability: Ability) {
        effectSummary.getDamageSources().forEach { (target, sources) ->
            val objectSheetBehavior = target.getBehavior<ObjectSheetBehavior>()

            if (objectSheetBehavior != null) {
                val isCritical = objectSheetBehavior.isCriticalHit(ability.source)

                objectSheetBehavior.applyDamages(target, sources, isCritical)
            }
        }
    }
}