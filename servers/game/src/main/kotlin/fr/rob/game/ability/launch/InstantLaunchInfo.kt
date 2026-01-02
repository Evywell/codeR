package fr.rob.game.ability.launch

import fr.rob.game.ability.Ability
import fr.rob.game.ability.trigger.AbilityTriggerInterface

class InstantLaunchInfo(private val onLaunchTrigger: AbilityTriggerInterface) : LaunchInfoInterface {
    override fun createAbilityLauncher(ability: Ability): LaunchTypeInterface = InstantLaunchType(ability, onLaunchTrigger)
}
