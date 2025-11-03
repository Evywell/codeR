package fr.rob.game.ability.launch

import fr.rob.game.ability.Ability

class InstantLaunchType(
    private val ability: Ability,
) : LaunchTypeInterface {
    private var isDone = false

    override fun handleLaunch() {
        isDone = true
        ability.resume(0)
    }

    override fun isLaunchingCompleted(): Boolean = isDone
}
