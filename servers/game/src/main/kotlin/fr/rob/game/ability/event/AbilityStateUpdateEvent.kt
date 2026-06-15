package fr.rob.game.ability.event

import fr.rob.game.ability.Ability
import fr.rob.game.event.DomainEventInterface

data class AbilityStateUpdateEvent(
    val oldState: Ability.AbilityState,
    val newState: Ability.AbilityState,
    val ability: Ability,
) : DomainEventInterface