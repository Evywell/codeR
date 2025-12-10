package fr.rob.game.domain.ability.resource

import fr.rob.game.domain.entity.WorldObject

interface ResourceTypeInterface {
    fun hasEnoughResources(worldObject: WorldObject): Boolean

    fun computeResources(worldObject: WorldObject)
}
