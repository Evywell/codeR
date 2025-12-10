package fr.rob.game.domain.ability.launch

import fr.rob.game.domain.ability.Ability

interface LaunchInfoInterface {
    fun createAbilityLauncher(ability: Ability): LaunchTypeInterface
}
