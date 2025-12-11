package fr.rob.game.combat

import fr.rob.game.entity.WorldObject
import fr.rob.game.entity.behavior.ObjectSheetTrait
import fr.rob.game.event.UniqueDomainEventInterface

data class ObjectSheetUpdated(val subject: WorldObject, val sheet: ObjectSheetTrait) : UniqueDomainEventInterface
