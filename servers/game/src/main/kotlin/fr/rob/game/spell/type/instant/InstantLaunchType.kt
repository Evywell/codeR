package fr.rob.game.spell.type.instant

import fr.rob.game.spell.Spell
import fr.rob.game.spell.type.LaunchTypeInterface

class InstantLaunchType(private val launchInfo: InstantLaunchInfo) : LaunchTypeInterface {
    override fun handleLaunch(spell: Spell) {
        launchInfo.onLaunchTrigger.trigger(spell)
    }

    override fun getCurrentState(): Spell.SpellState = Spell.SpellState.EFFECT_APPLIED
}
