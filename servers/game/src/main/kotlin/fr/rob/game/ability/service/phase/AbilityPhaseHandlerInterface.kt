package fr.rob.game.ability.service.phase

import fr.rob.game.ability.Ability

interface AbilityPhaseHandlerInterface {
    fun canHandle(ability: Ability): Boolean

    fun handle(ability: Ability, deltaTime: Int)
}
