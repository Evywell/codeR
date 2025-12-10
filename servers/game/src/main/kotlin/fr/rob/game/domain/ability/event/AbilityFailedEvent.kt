package fr.rob.game.domain.ability.event

import fr.rob.game.domain.ability.Ability
import fr.rob.game.domain.event.DomainEventInterface

data class AbilityFailedEvent(
    val ability: Ability,
    val reason: String,
) : DomainEventInterface
