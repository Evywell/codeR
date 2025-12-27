package fr.rob.game.ability.launch

import fr.rob.game.ability.Ability

interface LaunchInfoInterface {
    fun createAbilityLauncher(ability: Ability): LaunchTypeInterface
}
