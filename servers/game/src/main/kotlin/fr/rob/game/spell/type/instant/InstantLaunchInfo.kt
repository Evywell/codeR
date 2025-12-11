package fr.rob.game.spell.type.instant

import fr.rob.game.spell.trigger.SpellTriggerInterface
import fr.rob.game.spell.type.LaunchInfoInterface
import fr.rob.game.spell.type.LaunchTypeInterface

class InstantLaunchInfo(val onLaunchTrigger: SpellTriggerInterface) : LaunchInfoInterface {
    override fun createSpellLauncher(): LaunchTypeInterface = InstantLaunchType(this)
}
