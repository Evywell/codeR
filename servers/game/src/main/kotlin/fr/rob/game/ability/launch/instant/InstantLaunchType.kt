package fr.rob.game.ability.launch.instant

import fr.rob.game.ability.launch.LaunchTypeInterface

class InstantLaunchType : LaunchTypeInterface {
    private var isDone = false

    override fun handleLaunch() {
        if (isDone) {
            return
        }

        isDone = true
    }

    override fun isLaunchingCompleted(): Boolean = isDone
}
