package fr.rob.game.behavior

import fr.rob.game.entity.WorldObject
import kotlin.reflect.KClass

class BehaviorSet {
    private val behaviors = mutableMapOf<KClass<*>, BehaviorInterface>()

    fun addBehavior(behavior: BehaviorInterface) {
        behaviors[behavior::class] = behavior
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : BehaviorInterface> get(type: KClass<T>): T? {
        return behaviors[type] as T?
    }

    fun update(worldObject: WorldObject, deltaTime: Int) {
        behaviors.values.forEach { behavior ->
            if (behavior.canApplyTo(worldObject)) {
                behavior.update(worldObject, deltaTime)
            }
        }
    }
}