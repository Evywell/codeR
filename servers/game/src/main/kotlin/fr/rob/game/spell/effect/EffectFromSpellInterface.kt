package fr.rob.game.spell.effect

import fr.rob.game.spell.Spell

interface EffectFromSpellInterface {
    fun createEffectFromSpell(spell: Spell): EffectInterface
}
