package fr.rob.game.spell.trigger

import fr.rob.game.spell.Spell

interface SpellTriggerInterface {
    fun trigger(spell: Spell)
}
