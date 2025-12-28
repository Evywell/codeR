package fr.rob.game.spell.trigger

import fr.rob.game.behavior.ObjectSheetBehavior
import fr.rob.game.spell.Spell
import fr.rob.game.spell.SpellEffectInfo
import fr.rob.game.spell.SpellEffectSummary
import fr.rob.game.spell.effect.EffectFromSpellInterface

class ApplyEffectsSpellTrigger(private val effects: Array<SpellEffectInfo>) : SpellTriggerInterface {
    override fun trigger(spell: Spell) {
        handleApplyEffectsPhase(spell)
    }

    private fun handleApplyEffectsPhase(spell: Spell) {
        val spellEffectSummary = SpellEffectSummary()

        effects.forEach {
            applyEffect(spell, it, spellEffectSummary)
        }

        handleSpellDamages(spellEffectSummary, spell)
    }

    private fun handleSpellDamages(
        spellEffectSummary: SpellEffectSummary,
        spell: Spell
    ) {
        spellEffectSummary.forEach { (target, sources) ->
            // @todo hit check
            val objectSheetBehavior = target.getBehavior<ObjectSheetBehavior>()

            if (objectSheetBehavior != null) {
                val isCritical = objectSheetBehavior.isCriticalHit(spell.caster)

                objectSheetBehavior.applyDamages(target, sources, isCritical)
            }
        }
    }

    private fun applyEffect(spell: Spell, effectInfo: SpellEffectInfo, spellEffectSummary: SpellEffectSummary) {
        if (effectInfo is EffectFromSpellInterface) {
            effectInfo.createEffectFromSpell(spell).cast(spellEffectSummary)
        }
    }
}
