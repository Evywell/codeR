package fr.rob.game.domain.spell.type.instant

import fr.rob.game.domain.spell.trigger.SpellTriggerInterface
import fr.rob.game.domain.spell.type.LaunchInfoInterface
import fr.rob.game.domain.spell.type.LaunchTypeInterface

class InstantLaunchInfo(val onLaunchTrigger: SpellTriggerInterface) : LaunchInfoInterface {
    override fun createSpellLauncher(): LaunchTypeInterface = InstantLaunchType(this)
}
