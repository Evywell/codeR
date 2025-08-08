package fr.rob.game.ability.resource

import fr.rob.game.entity.WorldObject
import fr.rob.game.entity.behavior.HoldPowerResourcesTrait

class ManaResourceType(
    private val cost: Int,
) : ResourceTypeInterface {
    override fun hasEnoughResources(worldObject: WorldObject): Boolean {
        val trait = worldObject.getTrait<HoldPowerResourcesTrait>()

        if (trait.isEmpty) {
            return false
        }

        return trait.get().manaPower.value >= cost
    }

    override fun computeResources(worldObject: WorldObject) {
        val trait = worldObject.getTrait<HoldPowerResourcesTrait>()

        if (trait.isEmpty) {
            return
        }

        trait
            .get()
            .manaPower
            .reduce(cost)
    }
}
