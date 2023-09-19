package fr.rob.game.domain.spell.effect

import fr.rob.game.domain.entity.Unit
import fr.rob.game.domain.entity.behavior.HealthResourceTrait
import fr.rob.game.domain.spell.Spell
import fr.rob.game.domain.spell.target.SpellTargetParameter

class InstantDamageEffect(
    private val spellEffectInfo: InstantDamageEffectInfo,
    private val target: SpellTargetParameter,
    private val casterLevel: Int,
) : EffectInterface {
    override fun cast() {
        target.getPrimaryTarget().ifPresent { tar ->
            tar.getTrait(HealthResourceTrait::class).ifPresent {
                it.applyDamages(spellEffectInfo.value * casterLevel)
            }
        }
    }

    class InstantDamageEffectInfo(val value: Int) : SpellEffectInfo(SpellEffectTypeEnum.INSTANT_DAMAGE), EffectFromSpellInterface {
        override fun createEffectFromSpell(spell: Spell): EffectInterface =
            InstantDamageEffect(this, spell.target, (spell.caster as Unit).level)
    }
}
