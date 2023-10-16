package fr.rob.game.domain.spell.effect

import fr.rob.game.domain.spell.SpellEffectSummary

interface EffectInterface {
    fun cast(spellEffectSummary: SpellEffectSummary)
}
