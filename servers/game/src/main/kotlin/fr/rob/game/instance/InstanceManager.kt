package fr.rob.game.instance

import fr.rob.game.map.grid.GridBuilder
import fr.rob.game.map.Map

class InstanceManager(private val gridBuilder: GridBuilder) {

    private val instances = ArrayList<MapInstance>()

    fun create(id: Int, map: Map): MapInstance {
        val instance = MapInstance(
            id,
            map,
            gridBuilder.buildGrid(GridBuilder.DEFAULT_CELL_SIZE, map.zoneInfo.width, map.zoneInfo.height),
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

    fun retrieveByMap(mapId: Int, zoneId: Int): MapInstance {
        for (instance in instances) {
            if (instance.map.id == mapId && instance.map.zoneId == zoneId) {
                return instance
            }
        }

        throw RuntimeException("Cannot retrieve instance for map=$mapId zone=$zoneId")
    }

    fun getAllInstances(): List<MapInstance> = instances
}
