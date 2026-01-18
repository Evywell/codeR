package fr.rob.game.ability.effect

import fr.rob.game.ability.Ability
import fr.rob.game.combat.DamageSource
import fr.rob.game.entity.WorldObject

class InstantDamageEffect(
    private val effectInfo: InstantDamageEffectInfo,
    private val target: WorldObject?,
    private val caster: WorldObject,
) : EffectInterface {
    override fun apply(effectSummary: AbilityEffectSummary) {
        target?.let { tar ->
            effectSummary.addDamageSourceForTarget(tar, DamageSource(caster, effectInfo.damageValue))
        }
    }

    class InstantDamageEffectInfo(val damageValue: Int) : AbilityEffectInfoInterface {
        override fun createEffectFromAbility(abilityEffectInfo: Ability): EffectInterface =
            InstantDamageEffect(
                this,
                abilityEffectInfo.target.getPrimaryTarget(),
                abilityEffectInfo.source,
            )
    }
}
