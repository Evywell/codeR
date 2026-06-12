package fr.rob.game.ability.launch.projectile

import fr.rob.game.ability.Ability
import fr.rob.game.ability.launch.LaunchInfoInterface
import fr.rob.game.ability.launch.LaunchType
import fr.rob.game.ability.launch.LaunchTypeInterface

class FixedTimeProjectileLaunchInfo : LaunchInfoInterface {
    override fun support(ability: Ability): Boolean = ability.info.launchType.typeName == LaunchType.Name.FIXED_TIME_PROJECTILE

    override fun createAbilityLauncher(ability: Ability): LaunchTypeInterface =
        FixedTimeProjectileLaunchType()
}
