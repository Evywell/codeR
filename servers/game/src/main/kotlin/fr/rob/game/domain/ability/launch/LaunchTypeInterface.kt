package fr.rob.game.domain.ability.launch

interface LaunchTypeInterface {
    fun handleLaunch()

    fun isLaunchingCompleted(): Boolean
}
