package fr.rob.game.map.grid

import fr.rob.game.entity.WorldObject
import kotlin.reflect.KClass

class CellComponentIndex {
    private val componentToObjects = mutableMapOf<KClass<*>, MutableSet<WorldObject>>()

    fun addWorldObject(obj: WorldObject) {
        for (componentType in obj.getComponentTypes()) {
            componentToObjects.getOrPut(componentType) {
                mutableSetOf()
            }.add(obj)
        }
    }

    fun removeWorldObject(obj: WorldObject) {
        for (objects in componentToObjects.values) {
            objects.remove(obj)
        }
    }

    fun updateWorldObject(obj: WorldObject) {
        removeWorldObject(obj)
        addWorldObject(obj)
    }

    fun getObjectsWithComponent(componentType: KClass<*>): Set<WorldObject> {
        return componentToObjects[componentType] ?: emptySet()
    }

    fun getObjectsWithAllComponents(vararg componentTypes: KClass<*>): Set<WorldObject> {
        if (componentTypes.isEmpty()) return emptySet()

        val firstComponentType = componentToObjects[componentTypes[0]] ?: return emptySet()

        if (componentTypes.size == 1) return firstComponentType

        return firstComponentType.filter { obj ->
            componentTypes.all { componentType ->
                componentToObjects[componentType]?.contains(obj) == true
            }
        }.toSet()
    }
}