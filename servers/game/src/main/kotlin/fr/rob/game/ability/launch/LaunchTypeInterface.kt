package fr.rob.game.ability.launch

import fr.rob.game.ability.Ability

interface LaunchTypeInterface {
    fun handleLaunch()

    fun isLaunchingCompleted(): Boolean
}
