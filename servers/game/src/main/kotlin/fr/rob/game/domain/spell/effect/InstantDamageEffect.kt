package fr.rob.game.domain.spell.effect

import fr.rob.game.domain.combat.DamageSource
import fr.rob.game.domain.entity.WorldObject
import fr.rob.game.domain.spell.Spell
import fr.rob.game.domain.spell.SpellEffectInfo
import fr.rob.game.domain.spell.SpellEffectSummary
import fr.rob.game.domain.spell.SpellEffectTypeEnum
import fr.rob.game.domain.spell.target.SpellTargetParameter

class InstantDamageEffect(
    private val spellEffectInfo: InstantDamageEffectInfo,
    private val target: SpellTargetParameter,
    private val caster: WorldObject,
) : EffectInterface {
    override fun cast(spellEffectSummary: SpellEffectSummary) {
        target.getPrimaryTarget().ifPresent { tar ->
            spellEffectSummary.addDamageSourceForTarget(tar, DamageSource(caster, spellEffectInfo.value))
        }
    }

    class InstantDamageEffectInfo(val value: Int) : SpellEffectInfo(SpellEffectTypeEnum.INSTANT_DAMAGE), EffectFromSpellInterface {
        override fun createEffectFromSpell(spell: Spell): EffectInterface =
            InstantDamageEffect(this, spell.target, spell.caster)
    }
}
