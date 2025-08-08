package fr.rob.game.ability.resource

import fr.rob.game.entity.WorldObject

interface ResourceTypeInterface {
    fun hasEnoughResources(worldObject: WorldObject): Boolean

    fun computeResources(worldObject: WorldObject)
}
