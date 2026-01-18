package fr.rob.game.ability.launch

interface LaunchTypeInterface {
    fun handleLaunch()

    fun isLaunchingCompleted(): Boolean
}
