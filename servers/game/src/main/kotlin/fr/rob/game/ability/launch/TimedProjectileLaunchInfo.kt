package fr.rob.game.ability.launch

import fr.rob.game.ability.Ability
import fr.rob.game.ability.trigger.AbilityTriggerInterface

class TimedProjectileLaunchInfo(
    val travelTimeMs: Int,
    val onHitTargetTrigger: AbilityTriggerInterface,
) : LaunchInfoInterface {
    override fun createAbilityLauncher(ability: Ability): LaunchTypeInterface =
        TimedProjectileLaunchType(this)
}
