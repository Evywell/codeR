package fr.rob.game.test.unit.tools

import fr.rob.game.entity.Position
import fr.rob.game.entity.WorldObject
import fr.rob.game.entity.event.AddedIntoWorldEvent
import fr.rob.game.instance.MapInstance
import fr.rob.game.map.grid.GridBuilder
import fr.rob.game.map.grid.GridConstraintChecker
import fr.rob.game.map.grid.chunk.ChunkManager
import fr.rob.game.map.Map
import fr.rob.game.map.MapInfo
import fr.rob.game.map.ZoneInfo

class WorldBuilder {
    companion object {
        private val chunkManagers = mutableMapOf<Int, ChunkManager>()

        fun buildBasicWorld(): MapInstance {
            val mapInfo = MapInfo("A testing map", 200, 200)
            val zoneInfo = ZoneInfo("A testing zone", 100, 100, 0f, 0f)
            val map = Map(1, 1, mapInfo, zoneInfo)
            val gridBuilder = GridBuilder(GridConstraintChecker())
            val grid = gridBuilder.buildGrid(10, zoneInfo.width, zoneInfo.height)
            val chunkManager = ChunkManager(grid, ChunkManager.DEFAULT_CHUNK_SIZE)

            val instance = MapInstance(13, map, grid)
            chunkManagers[instance.id] = chunkManager

            return instance
        }

        fun addIntoInstance(worldObject: WorldObject, instance: MapInstance, position: Position) {
            worldObject.mapInstance = instance
            worldObject.position = position
            worldObject.isInWorld = true

            instance.pushEvent(AddedIntoWorldEvent(worldObject))
            instance.grid.addWorldObject(worldObject)

            val chunkManager = chunkManagers[instance.id]
            if (chunkManager != null) {
                val chunkId = chunkManager.getChunkIdForPosition(position)
                worldObject.cachedChunkId = chunkId
                chunkManager.registerEntity(worldObject, chunkId)

                if (worldObject.guid.isPlayer()) {
                    chunkManager.onPlayerEntered(worldObject)
                }
            }
        }

        fun registerChunkManager(instanceId: Int, chunkManager: ChunkManager) {
            chunkManagers[instanceId] = chunkManager
        }
    }
}
