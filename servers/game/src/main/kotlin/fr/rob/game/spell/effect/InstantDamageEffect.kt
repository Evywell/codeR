package fr.rob.game.spell.effect

import fr.rob.game.combat.DamageSource
import fr.rob.game.entity.WorldObject
import fr.rob.game.spell.Spell
import fr.rob.game.spell.SpellEffectInfo
import fr.rob.game.spell.SpellEffectSummary
import fr.rob.game.spell.SpellEffectTypeEnum
import fr.rob.game.spell.target.SpellTargetParameter

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
