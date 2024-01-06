package fr.rob.game.domain.combat

import fr.rob.game.domain.entity.WorldObject
import fr.rob.game.domain.event.DomainEventInterface

data class HealthUpdatedEvent(val worldObject: WorldObject, val newHealth: Int) : DomainEventInterface
