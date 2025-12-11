package fr.rob.game.spell.type

import fr.rob.game.spell.Spell

interface LaunchTypeInterface {
    fun handleLaunch(spell: Spell)
    fun getCurrentState(): Spell.SpellState
}
