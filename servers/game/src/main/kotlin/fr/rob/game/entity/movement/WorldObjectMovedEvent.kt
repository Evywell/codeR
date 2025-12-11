package fr.rob.game.entity.movement

import fr.rob.game.entity.WorldObject
import fr.rob.game.event.DomainEventInterface

data class WorldObjectMovedEvent(val worldObject: WorldObject) : DomainEventInterface
