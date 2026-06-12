package fr.rob.game.ability.launch.instant

import fr.rob.game.ability.Ability
import fr.rob.game.ability.launch.LaunchInfoInterface
import fr.rob.game.ability.launch.LaunchType
import fr.rob.game.ability.launch.LaunchTypeInterface

class InstantLaunchInfo : LaunchInfoInterface {
    override fun support(ability: Ability): Boolean = ability.info.launchType.typeName == LaunchType.Name.INSTANT

    override fun createAbilityLauncher(ability: Ability): LaunchTypeInterface =
        InstantLaunchType()
}
