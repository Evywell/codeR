package fr.rob.game.ability.event

import fr.rob.game.ability.Ability
import fr.rob.game.event.DomainEventInterface

data class AbilityFailedEvent(
    val ability: Ability,
    val reason: String,
) : DomainEventInterface
