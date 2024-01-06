package fr.rob.game.domain.combat

import fr.rob.game.domain.entity.WorldObject
import fr.rob.game.domain.entity.behavior.ObjectSheetTrait
import fr.rob.game.domain.event.UniqueDomainEventInterface

data class ObjectSheetUpdated(val subject: WorldObject, val sheet: ObjectSheetTrait) : UniqueDomainEventInterface
