package fr.rob.game.domain.entity.movement

import fr.rob.game.domain.entity.WorldObject
import fr.rob.game.domain.event.DomainEventInterface

data class WorldObjectMovedEvent(val worldObject: WorldObject) : DomainEventInterface
