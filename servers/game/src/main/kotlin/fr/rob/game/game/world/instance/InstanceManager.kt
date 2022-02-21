package fr.rob.game.game.world.instance

import fr.rob.game.game.world.map.Map

class InstanceManager {

    private val instances = ArrayList<MapInstance>()

    fun create(id: Int, map: Map): MapInstance {
        val instance = MapInstance(id, map)
        instances.add(instance)

        return instance
    }

    fun update(deltaTime: Int) {
        for (instance in instances) {
            instance.update(deltaTime)
        }
    }

    fun getAllInstances(): List<MapInstance> = instances
}
