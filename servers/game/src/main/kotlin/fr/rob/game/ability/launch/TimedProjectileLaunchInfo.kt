package fr.rob.game.ability.launch

import fr.rob.game.ability.Ability
import fr.rob.game.ability.AbilityInfo

class TimedProjectileLaunchInfo(val travelTimeMs: Int) : LaunchInfoInterface {
    override fun support(ability: Ability): Boolean = ability.info.launchType == AbilityInfo.LaunchType.TIMED_PROJECTILE

    override fun createAbilityLauncher(ability: Ability): LaunchTypeInterface =
        TimedProjectileLaunchType(this)
}
