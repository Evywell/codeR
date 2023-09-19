package fr.rob.game.domain.spell.effect

import fr.rob.game.domain.spell.Spell

interface EffectFromSpellInterface {
    fun createEffectFromSpell(spell: Spell): EffectInterface
}
