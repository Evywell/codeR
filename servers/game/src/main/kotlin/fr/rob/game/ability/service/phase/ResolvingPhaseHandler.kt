package fr.rob.game.ability.service.phase

import fr.rob.game.ability.Ability
import fr.rob.game.ability.effect.AbilityEffectSummary
import fr.rob.game.behavior.ObjectSheetBehavior

class ResolvingPhaseHandler : AbilityPhaseHandlerInterface {
    override fun canHandle(ability: Ability): Boolean = ability.state == Ability.AbilityState.RESOLVING

    override fun handle(ability: Ability, deltaTime: Int) {
        val effectSummary = AbilityEffectSummary()
        val effectsInfo = ability.info.effectsInfo

        for (effectInfo in effectsInfo) {
            val effect = effectInfo.createEffectFromAbility(ability)
            effect.apply(effectSummary)
        }

        effectSummary.getDamageSources().forEach { (target, sources) ->
            val objectSheetBehavior = target.getBehavior<ObjectSheetBehavior>()

            if (objectSheetBehavior != null) {
                val isCritical = objectSheetBehavior.isCriticalHit(ability.source)

                objectSheetBehavior.applyDamages(target, sources, isCritical)
            }
        }

        ability.state = Ability.AbilityState.DONE
    }
}