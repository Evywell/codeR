package fr.rob.game.domain.ability.launch

import fr.rob.game.domain.ability.Ability

interface LaunchTypeInterface {
    fun handleLaunch(ability: Ability)
}
