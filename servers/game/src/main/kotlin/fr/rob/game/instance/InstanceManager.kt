package fr.rob.game.instance

import fr.rob.game.map.grid.GridBuilder
import fr.rob.game.map.grid.chunk.ChunkManager
import fr.rob.game.map.Map

class InstanceManager(private val gridBuilder: GridBuilder) {

    private val instances = ArrayList<MapInstance>()
    private val chunkManagers = mutableMapOf<Int, ChunkManager>()

    fun create(id: Int, map: Map): MapInstance {
        val grid = gridBuilder.buildGrid(GridBuilder.DEFAULT_CELL_SIZE, map.zoneInfo.width, map.zoneInfo.height)
        val chunkManager = ChunkManager(grid, ChunkManager.DEFAULT_CHUNK_SIZE)

        val instance = MapInstance(id, map, grid)
        instances.add(instance)
        registerChunkManager(id, chunkManager)

        return instance
    }

    fun getChunkManager(instanceId: Int): ChunkManager =
        chunkManagers[instanceId] ?: throw RuntimeException("No ChunkManager for instance $instanceId")

    fun registerChunkManager(instanceId: Int, chunkManager: ChunkManager) {
        chunkManagers[instanceId] = chunkManager
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
