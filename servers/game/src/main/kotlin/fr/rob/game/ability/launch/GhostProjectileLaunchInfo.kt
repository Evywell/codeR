package fr.rob.game.ability.launch

import fr.rob.game.ability.Ability
import fr.rob.game.ability.trigger.AbilityTriggerInterface

class GhostProjectileLaunchInfo(
    val projectileSpeed: Float,
    val onHitTargetTrigger: AbilityTriggerInterface,
) : LaunchInfoInterface {
    override fun createAbilityLauncher(ability: Ability): LaunchTypeInterface =
        GhostProjectileLaunchType(this)
}
