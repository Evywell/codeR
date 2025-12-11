package fr.rob.game.entity.event

import fr.rob.game.entity.WorldObject
import fr.rob.game.event.DomainEventInterface

data class AddedIntoWorldEvent(val worldObject: WorldObject) : DomainEventInterface
