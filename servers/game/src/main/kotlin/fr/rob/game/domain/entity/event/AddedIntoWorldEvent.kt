package fr.rob.game.domain.entity.event

import fr.rob.game.domain.entity.WorldObject
import fr.rob.game.domain.event.DomainEventInterface

data class AddedIntoWorldEvent(val worldObject: WorldObject) : DomainEventInterface
