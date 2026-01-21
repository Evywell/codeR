package fr.rob.game.ability.launch

import fr.rob.game.ability.Ability
import fr.rob.game.ability.AbilityInfo

class InstantLaunchInfo : LaunchInfoInterface {
    override fun support(ability: Ability): Boolean = ability.info.launchType == AbilityInfo.LaunchType.INSTANT

    override fun createAbilityLauncher(ability: Ability): LaunchTypeInterface =
        InstantLaunchType()
}
