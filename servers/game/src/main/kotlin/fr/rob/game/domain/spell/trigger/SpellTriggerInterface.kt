package fr.rob.game.domain.spell.trigger

import fr.rob.game.domain.spell.Spell

interface SpellTriggerInterface {
    fun trigger(spell: Spell)
}
