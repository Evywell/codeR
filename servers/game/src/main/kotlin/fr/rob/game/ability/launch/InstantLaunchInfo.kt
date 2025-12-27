package fr.rob.game.ability.launch

import fr.rob.game.ability.Ability

class InstantLaunchInfo : LaunchInfoInterface {
    override fun createAbilityLauncher(ability: Ability): LaunchTypeInterface = InstantLaunchType(ability)
}
