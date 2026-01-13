package fr.rob.game.ability.launch

import fr.rob.game.ability.Ability

interface UpdatableLaunchTypeInterface : LaunchTypeInterface {
    fun update(deltaTime: Int, ability: Ability)
}
