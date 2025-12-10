package fr.rob.game.domain.ability.launch

import fr.rob.game.domain.ability.Ability

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
