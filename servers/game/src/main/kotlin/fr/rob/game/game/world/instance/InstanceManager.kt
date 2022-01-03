package fr.rob.game.game.world.instance

class InstanceManager {

    private val instances = ArrayList<MapInstance>()

    fun create(instance: MapInstance) {
        instances.add(instance)
    }

    fun update(deltaTime: Int) {
        for (instance in instances) {
            instance.update(deltaTime)
        }
    }

    fun getAllInstances(): List<MapInstance> = instances
}
