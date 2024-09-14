package fr.rob.game.domain.spell.type

import fr.rob.game.domain.spell.Spell

interface LaunchTypeInterface {
    fun handleLaunch(spell: Spell)
    fun getCurrentState(): Spell.SpellState
}
