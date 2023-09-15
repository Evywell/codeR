package fr.rob.game.domain.spell.effect

import fr.rob.game.domain.entity.behavior.HealthResourceTrait
import fr.rob.game.domain.spell.target.SpellTargetParameter

class InstantDamageEffect(
    private val spellEffectInfo: InstantDamageEffectInfo,
    private val target: SpellTargetParameter,
    private val casterLevel: Int,
) {
    fun cast() {
        target.getPrimaryTarget().ifPresent { tar ->
            tar.getTrait(HealthResourceTrait::class).ifPresent {
                it.applyDamages(spellEffectInfo.value * casterLevel)
            }
        }
    }

    class InstantDamageEffectInfo(val value: Int) : SpellEffectInfo(SpellEffectTypeEnum.INSTANT_DAMAGE)
}
