package fr.rob.game.ability.launch

import fr.rob.game.ability.Ability
import fr.rob.game.ability.AbilityInfo

class GhostProjectileLaunchInfo(val projectileSpeed: Float) : LaunchInfoInterface {
    override fun support(ability: Ability): Boolean = ability.info.launchType == AbilityInfo.LaunchType.TRACKED_PROJECTILE

    override fun createAbilityLauncher(ability: Ability): LaunchTypeInterface =
        GhostProjectileLaunchType(this)
}
