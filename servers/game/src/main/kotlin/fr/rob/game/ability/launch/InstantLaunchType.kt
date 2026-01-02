package fr.rob.game.ability.launch

import fr.rob.game.ability.Ability
import fr.rob.game.ability.trigger.AbilityTriggerInterface

class InstantLaunchType(
    private val ability: Ability,
    private val onLaunchTrigger: AbilityTriggerInterface
) : LaunchTypeInterface {
    private var isDone = false

    override fun handleLaunch() {
        isDone = true
        onLaunchTrigger.trigger(ability)
        ability.resume(0)
    }

    override fun isLaunchingCompleted(): Boolean = isDone
}
