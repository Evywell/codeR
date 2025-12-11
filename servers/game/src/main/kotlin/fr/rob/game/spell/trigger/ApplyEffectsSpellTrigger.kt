package fr.rob.game.spell.trigger

import fr.rob.game.entity.behavior.ObjectSheetTrait
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
        spellEffectSummary.forEach { target, sources ->
            // @todo hit check

            target.getTrait(ObjectSheetTrait::class).ifPresent {
                val isCritical = it.isCriticalHit(spell.caster)

                it.applyDamages(sources, isCritical)
            }
        }
    }

    private fun applyEffect(spell: Spell, effectInfo: SpellEffectInfo, spellEffectSummary: SpellEffectSummary) {
        if (effectInfo is EffectFromSpellInterface) {
            effectInfo.createEffectFromSpell(spell).cast(spellEffectSummary)
        }
    }
}
