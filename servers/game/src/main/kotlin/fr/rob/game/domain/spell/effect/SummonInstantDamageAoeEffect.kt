package fr.rob.game.domain.spell.effect

import fr.rob.game.domain.entity.ScriptedWorldObject
import fr.rob.game.domain.entity.WorldObject
import fr.rob.game.domain.entity.guid.ObjectGuidGenerator
import fr.rob.game.domain.script.InstantDamageAoeScript
import fr.rob.game.domain.spell.Spell
import fr.rob.game.domain.spell.SpellEffectInfo
import fr.rob.game.domain.spell.SpellEffectSummary
import fr.rob.game.domain.spell.SpellEffectTypeEnum
import fr.rob.game.domain.spell.SpellInfo

class SummonInstantDamageAoeEffect(
    private val spellEffectInfo: SummonInstantDamageAoeEffectInfo,
    private val spellInfo: SpellInfo,
    private val caster: WorldObject,
) : EffectInterface {
    private val guidGenerator = ObjectGuidGenerator()

    override fun cast(spellEffectSummary: SpellEffectSummary) {
        val area = ScriptedWorldObject(
            guidGenerator.createForScriptableObject(caster, spellInfo.identifier),
        )

        area.addScript(InstantDamageAoeScript(area, caster, spellEffectInfo.damageValue, spellEffectInfo.radius, spellEffectInfo.frequencyMs, spellEffectInfo.durationMs))
        area.addIntoInstance(caster.mapInstance, caster.position)
    }

    class SummonInstantDamageAoeEffectInfo(val damageValue: Int, val radius: Float, val frequencyMs: Int, val durationMs: Int) :
        SpellEffectInfo(SpellEffectTypeEnum.SUMMON_AREA), EffectFromSpellInterface {
        override fun createEffectFromSpell(spell: Spell): EffectInterface =
            SummonInstantDamageAoeEffect(this, spell.spellInfo, spell.caster)
    }
}
