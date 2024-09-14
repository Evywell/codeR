package fr.rob.game.domain.spell.type.instant

import fr.rob.game.domain.spell.Spell
import fr.rob.game.domain.spell.type.LaunchTypeInterface

class InstantLaunchType(private val launchInfo: InstantLaunchInfo) : LaunchTypeInterface {
    override fun handleLaunch(spell: Spell) {
        launchInfo.onLaunchTrigger.trigger(spell)
    }

    override fun getCurrentState(): Spell.SpellState = Spell.SpellState.EFFECT_APPLIED
}
