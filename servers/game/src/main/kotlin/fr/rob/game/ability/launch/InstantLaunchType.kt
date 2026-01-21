package fr.rob.game.ability.launch

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
