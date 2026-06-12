package fr.rob.game.ability.launch.projectile

import fr.rob.game.ability.Ability
import fr.rob.game.ability.launch.LaunchInfoInterface
import fr.rob.game.ability.launch.LaunchType
import fr.rob.game.ability.launch.LaunchTypeInterface

class GhostProjectileLaunchInfo(val projectileSpeed: Float) : LaunchInfoInterface {
    override fun support(ability: Ability): Boolean = ability.info.launchType.typeName == LaunchType.Name.TRACKED_PROJECTILE

    override fun createAbilityLauncher(ability: Ability): LaunchTypeInterface =
        GhostProjectileLaunchType(this)
}
