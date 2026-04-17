package fr.rob.game.spell.effect

import fr.rob.game.entity.ScriptedWorldObject
import fr.rob.game.entity.WorldObject
import fr.rob.game.entity.event.AddedIntoWorldEvent
import fr.rob.game.entity.guid.ObjectGuidGenerator
import fr.rob.game.script.InstantDamageAoeScript
import fr.rob.game.spell.Spell
import fr.rob.game.spell.SpellEffectInfo
import fr.rob.game.spell.SpellEffectSummary
import fr.rob.game.spell.SpellEffectTypeEnum
import fr.rob.game.spell.SpellInfo

/**
 * @deprecated The spell system is deprecated; use the ability system instead.
 * This effect bypasses [ObjectManager.addEntityIntoInstance] because DI is not
 * wired through the spell factory chain. Chunk registration is skipped.
 */
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

        // Inline placement — deprecated spell system cannot use ObjectManager (no DI path).
        // Chunk registration is intentionally skipped; ScriptedWorldObjects use the classic loop.
        area.mapInstance = caster.mapInstance
        area.position = caster.position
        area.isInWorld = true
        caster.mapInstance.pushEvent(AddedIntoWorldEvent(area))
        caster.mapInstance.grid.addWorldObject(area)
    }

    class SummonInstantDamageAoeEffectInfo(val damageValue: Int, val radius: Float, val frequencyMs: Int, val durationMs: Int) :
        SpellEffectInfo(SpellEffectTypeEnum.SUMMON_AREA), EffectFromSpellInterface {
        override fun createEffectFromSpell(spell: Spell): EffectInterface =
            SummonInstantDamageAoeEffect(this, spell.spellInfo, spell.caster)
    }
}
