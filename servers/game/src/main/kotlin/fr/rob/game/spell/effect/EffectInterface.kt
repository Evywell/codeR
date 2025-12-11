package fr.rob.game.spell.effect

import fr.rob.game.spell.SpellEffectSummary

interface EffectInterface {
    fun cast(spellEffectSummary: SpellEffectSummary)
}
