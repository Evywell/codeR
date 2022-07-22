package fr.rob.game.domain.instance

import fr.rob.game.domain.terrain.grid.GridBuilder
import fr.rob.game.domain.terrain.map.Map

class InstanceManager(private val gridBuilder: GridBuilder) {

    private val instances = ArrayList<MapInstance>()

    fun create(id: Int, map: Map): MapInstance {
        val instance = MapInstance(
            id,
            map,
            gridBuilder.buildGrid(GridBuilder.DEFAULT_CELL_SIZE, map.zoneInfo.width, map.zoneInfo.height)
        )
        instances.add(instance)

        return instance
    }

    fun retrieve(id: Int): MapInstance {
        for (instance in instances) {
            if (instance.id == id) {
                return instance
            }
        }

        throw RuntimeException("Cannot find instance with id $id")
    }

    fun update(deltaTime: Int) {
        for (instance in instances) {
            // instance.update(deltaTime)
        }
    }

    fun getAllInstances(): List<MapInstance> = instances
}
