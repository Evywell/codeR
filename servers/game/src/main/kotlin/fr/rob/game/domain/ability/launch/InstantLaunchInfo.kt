package fr.rob.game.domain.ability.launch

import fr.rob.game.domain.ability.Ability

class InstantLaunchInfo : LaunchInfoInterface {
    override fun createAbilityLauncher(ability: Ability): LaunchTypeInterface = InstantLaunchType(ability)
}
