package fr.rob.game.ability.resource

import fr.rob.game.component.resource.ManaComponent
import fr.rob.game.entity.WorldObject

class ManaResourceType(
    private val cost: Int,
) : ResourceTypeInterface {
    override fun hasEnoughResources(worldObject: WorldObject): Boolean {
        val resource = worldObject.getComponent<ManaComponent>() ?: return false

        return resource.hasEnough(cost)
    }

    override fun computeResources(worldObject: WorldObject) {
        val resource = worldObject.getComponent<ManaComponent>() ?: return

        resource.reduce(cost)
    }
}
