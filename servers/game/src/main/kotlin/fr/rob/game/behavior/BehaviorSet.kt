package fr.rob.game.behavior

import fr.rob.game.entity.WorldObject

class BehaviorSet {
    private val behaviors = mutableSetOf<BehaviorInterface>()

    fun addBehavior(behavior: BehaviorInterface) {
        behaviors.add(behavior)
    }

    fun update(worldObject: WorldObject, deltaTime: Int) {
        behaviors.forEach { behavior ->
            if (behavior.canApplyTo(worldObject)) {
                behavior.update(worldObject, deltaTime)
            }
        }
    }
}