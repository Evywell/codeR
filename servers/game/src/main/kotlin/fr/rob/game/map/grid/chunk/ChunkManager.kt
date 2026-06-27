package fr.rob.game.map.grid.chunk

import fr.rob.game.entity.WorldObject
import fr.rob.game.entity.guid.ObjectGuid
import fr.rob.game.map.grid.Grid

class ChunkManager(
    private val grid: Grid,
    private val chunkSize: Int,
    private val shutdownDurationMs: Int = DEFAULT_SHUTDOWN_DURATION_MS,
) {
    private val chunkStates = mutableMapOf<ChunkId, ChunkState>()
    private val playerCountPerChunk = mutableMapOf<ChunkId, Int>()

    private val entitiesByChunk = mutableMapOf<ChunkId, Array<LinkedHashSet<WorldObject>>>()

    fun getChunkIdForPosition(position: fr.rob.game.entity.Position): ChunkId {
        val cell = grid.getCellFromWorldPosition(position)
        return ChunkId(cell.x / chunkSize, cell.y / chunkSize)
    }

    fun registerEntity(entity: WorldObject, chunkId: ChunkId) {
        val typeSets = getOrCreateEntitySets(chunkId)
        typeSets[entity.guid.getType().value].add(entity)
    }

    fun unregisterEntity(entity: WorldObject, chunkId: ChunkId) {
        entitiesByChunk[chunkId]?.get(entity.guid.getType().value)?.remove(entity)
    }

    fun onPlayerEntered(player: WorldObject) {
        val newChunkId = getChunkIdForPosition(player.position)
        val nearbyChunks = getNeighborChunkIds(newChunkId)

        for (chunk in nearbyChunks) {
            incrementPlayerCount(chunk)
            activateChunk(chunk)
        }
    }

    fun onPlayerLeft(player: WorldObject) {
        val oldChunkId = player.cachedChunkId ?: return
        val nearbyChunks = getNeighborChunkIds(oldChunkId)

        for (chunk in nearbyChunks) {
            decrementPlayerCount(chunk)
        }
    }

    fun onPlayerChunkChanged(oldChunkId: ChunkId, newChunkId: ChunkId) {
        val oldNearby = getNeighborChunkIds(oldChunkId)
        val newNearby = getNeighborChunkIds(newChunkId)

        val chunksLost = oldNearby - newNearby.toSet()
        val chunksGained = newNearby - oldNearby.toSet()

        for (chunk in chunksLost) {
            decrementPlayerCount(chunk)
        }

        for (chunk in chunksGained) {
            incrementPlayerCount(chunk)
            activateChunk(chunk)
        }
    }

    /**
     * Ticks shutdown timers. Returns chunks that transitioned to Dormant.
     */
    fun update(deltaTime: Int): List<ChunkId> {
        val newlyDormant = mutableListOf<ChunkId>()

        val iterator = chunkStates.iterator()
        while (iterator.hasNext()) {
            val (chunkId, state) = iterator.next()

            if (state is ChunkState.ShuttingDown) {
                state.remainingMs -= deltaTime
                if (state.remainingMs <= 0) {
                    iterator.remove()
                    newlyDormant.add(chunkId)
                }
            }
        }

        return newlyDormant
    }

    fun despawnChunkEntities(chunkId: ChunkId): List<WorldObject> {
        val despawned = mutableListOf<WorldObject>()
        val typeSets = entitiesByChunk[chunkId] ?: return despawned

        for (typeIndex in typeSets.indices) {
            if (typeIndex == ObjectGuid.GUID_TYPE.PLAYER.value) continue

            val entities = typeSets[typeIndex]
            for (entity in entities) {
                grid.removeWorldObject(entity)
                entity.isInWorld = false
                despawned.add(entity)
            }
            entities.clear()
        }

        return despawned
    }

    fun getActiveEntitiesByType(type: ObjectGuid.GUID_TYPE): List<WorldObject> {
        val snapshot = ArrayList<WorldObject>()
        for ((chunkId, typeSets) in entitiesByChunk) {
            if (!isChunkActive(chunkId)) continue
            snapshot.addAll(typeSets[type.value])
        }
        return snapshot
    }

    fun getChunkState(chunkId: ChunkId): ChunkState =
        chunkStates[chunkId] ?: ChunkState.Dormant

    fun getPlayerCount(chunkId: ChunkId): Int =
        playerCountPerChunk[chunkId] ?: 0

    /**
     * Returns the chunk IDs in a 1-chunk radius around the given chunk
     * (including the chunk itself). Clamps to valid grid bounds.
     */
    fun getNeighborChunkIds(chunkId: ChunkId): List<ChunkId> {
        val maxChunkX = (grid.width - 1) / chunkSize
        val maxChunkY = (grid.height - 1) / chunkSize
        val neighbors = mutableListOf<ChunkId>()

        for (dx in -1..1) {
            for (dy in -1..1) {
                val nx = chunkId.x + dx
                val ny = chunkId.y + dy
                if (nx in 0..maxChunkX && ny in 0..maxChunkY) {
                    neighbors.add(ChunkId(nx, ny))
                }
            }
        }

        return neighbors
    }

    private fun isChunkActive(chunkId: ChunkId): Boolean {
        val state = chunkStates[chunkId] ?: return false
        return state is ChunkState.Active || state is ChunkState.ShuttingDown
    }

    private fun activateChunk(chunkId: ChunkId) {
        chunkStates[chunkId] = ChunkState.Active
    }

    private fun incrementPlayerCount(chunkId: ChunkId) {
        playerCountPerChunk[chunkId] = (playerCountPerChunk[chunkId] ?: 0) + 1
    }

    private fun decrementPlayerCount(chunkId: ChunkId) {
        val count = (playerCountPerChunk[chunkId] ?: 0) - 1

        if (count <= 0) {
            playerCountPerChunk.remove(chunkId)
            startShutdown(chunkId)
        } else {
            playerCountPerChunk[chunkId] = count
        }
    }

    private fun startShutdown(chunkId: ChunkId) {
        val currentState = chunkStates[chunkId]
        if (currentState is ChunkState.Active) {
            chunkStates[chunkId] = ChunkState.ShuttingDown(shutdownDurationMs)
        }
    }

    private fun getOrCreateEntitySets(chunkId: ChunkId): Array<LinkedHashSet<WorldObject>> =
        entitiesByChunk.getOrPut(chunkId) {
            Array(ObjectGuid.GUID_TYPE.values().size) { LinkedHashSet() }
        }

    companion object {
        const val DEFAULT_SHUTDOWN_DURATION_MS = 300_000
        const val DEFAULT_CHUNK_SIZE = 3
    }
}
