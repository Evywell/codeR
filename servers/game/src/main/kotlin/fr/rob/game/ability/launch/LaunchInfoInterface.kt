package fr.rob.game.ability.launch

import fr.rob.game.ability.Ability

interface LaunchInfoInterface {
    fun support(ability: Ability): Boolean
    fun createAbilityLauncher(ability: Ability): LaunchTypeInterface
}
