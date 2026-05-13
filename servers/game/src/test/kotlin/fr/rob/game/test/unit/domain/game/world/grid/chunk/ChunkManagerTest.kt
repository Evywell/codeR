package fr.rob.game.test.unit.domain.game.world.grid.chunk

import fr.rob.game.entity.Position
import fr.rob.game.entity.guid.ObjectGuid
import fr.rob.game.entity.guid.ObjectGuidGenerator
import fr.rob.game.map.grid.GridBuilder
import fr.rob.game.map.grid.GridConstraintChecker
import fr.rob.game.map.grid.chunk.ChunkId
import fr.rob.game.map.grid.chunk.ChunkManager
import fr.rob.game.map.grid.chunk.ChunkState
import fr.rob.game.test.unit.tools.DummyWorldObjectBuilder
import fr.rob.game.test.unit.tools.WorldBuilder
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

/**
 * Test grid: 10x10 cells, cell size 10m, zone 100x100m.
 * Chunk size: 3 cells => chunks (0,0)..(3,3).
 *
 * World origin (0,0) maps to zone center. With zone 100x100 and offset (0,0):
 *   normalizedX = worldX + 50, cellX = ceil(normalizedX / 10) - 1
 *
 * Cell layout per chunk:
 *   Chunk (0,0) = cells (0,0)-(2,2)
 *   Chunk (1,0) = cells (3,0)-(5,2)
 *   Chunk (3,3) = cell (9,9)
 */
class ChunkManagerTest {

    private val worldBuilder = WorldBuilder()
    private val grid = GridBuilder(GridConstraintChecker()).buildGrid(10, 100, 100)
    private val chunkManager = ChunkManager(grid, ChunkManager.DEFAULT_CHUNK_SIZE)
    private val instance = worldBuilder.buildInstanceWithChunkManager(grid, chunkManager, 13)
    private val objectBuilder = DummyWorldObjectBuilder(ObjectGuidGenerator())

    companion object {
        // World positions that map to known cells/chunks.
        // Formula: cellX = ceil((worldX + 50) / 10) - 1

        /** Cell (0,0) → chunk (0,0) */
        fun posInChunk00() = Position(-45f, -45f, 0f, 0f)

        /** Cell (1,0) → chunk (0,0) — same chunk, different cell */
        fun posInChunk00Alt() = Position(-35f, -45f, 0f, 0f)

        /** Cell (3,0) → chunk (1,0) */
        fun posInChunk10() = Position(-15f, -45f, 0f, 0f)

        /** Cell (9,9) → chunk (3,3) */
        fun posInChunk33() = Position(45f, 45f, 0f, 0f)

        /** Cell (5,5) → chunk (1,1) — center of grid */
        fun posInChunk11() = Position(5f, 5f, 0f, 0f)
    }

    // ──────────────────────────────────────────────
    // Chunk ID calculation
    // ──────────────────────────────────────────────

    @Nested
    inner class ChunkIdCalculation {
        @Test
        fun `position in first cell maps to chunk (0,0)`() {
            val chunkId = chunkManager.getChunkIdForPosition(posInChunk00())
            assertEquals(ChunkId(0, 0), chunkId)
        }

        @Test
        fun `position in cell (3,0) maps to chunk (1,0)`() {
            val chunkId = chunkManager.getChunkIdForPosition(posInChunk10())
            assertEquals(ChunkId(1, 0), chunkId)
        }

        @Test
        fun `position in cell (9,9) maps to chunk (3,3)`() {
            val chunkId = chunkManager.getChunkIdForPosition(posInChunk33())
            assertEquals(ChunkId(3, 3), chunkId)
        }
    }

    // ──────────────────────────────────────────────
    // Neighbor chunks
    // ──────────────────────────────────────────────

    @Nested
    inner class NeighborChunks {
        @Test
        fun `center chunk has 9 neighbors including itself`() {
            val neighbors = chunkManager.getNeighborChunkIds(ChunkId(1, 1))
            assertEquals(9, neighbors.size)
            assertTrue(neighbors.contains(ChunkId(1, 1)))
            assertTrue(neighbors.contains(ChunkId(0, 0)))
            assertTrue(neighbors.contains(ChunkId(2, 2)))
        }

        @Test
        fun `corner chunk (0,0) has 4 neighbors`() {
            val neighbors = chunkManager.getNeighborChunkIds(ChunkId(0, 0))
            assertEquals(4, neighbors.size)
            assertTrue(neighbors.contains(ChunkId(0, 0)))
            assertTrue(neighbors.contains(ChunkId(1, 0)))
            assertTrue(neighbors.contains(ChunkId(0, 1)))
            assertTrue(neighbors.contains(ChunkId(1, 1)))
        }

        @Test
        fun `edge chunk (1,0) has 6 neighbors`() {
            val neighbors = chunkManager.getNeighborChunkIds(ChunkId(1, 0))
            assertEquals(6, neighbors.size)
        }
    }

    // ──────────────────────────────────────────────
    // State transitions: player enters / leaves
    // ──────────────────────────────────────────────

    @Nested
    inner class StateTransitions {
        @Test
        fun `all chunks are dormant by default`() {
            assertEquals(ChunkState.Dormant, chunkManager.getChunkState(ChunkId(0, 0)))
            assertEquals(ChunkState.Dormant, chunkManager.getChunkState(ChunkId(1, 1)))
        }

        @Test
        fun `player entering activates their chunk and neighbors`() {
            val player = objectBuilder.createPlayer()
            worldBuilder.addIntoInstance(player, instance, posInChunk00())

            val neighbors = chunkManager.getNeighborChunkIds(ChunkId(0, 0))
            for (chunk in neighbors) {
                assertEquals(ChunkState.Active, chunkManager.getChunkState(chunk))
            }
        }

        @Test
        fun `player leaving starts shutdown on all nearby chunks`() {
            val player = objectBuilder.createPlayer()
            worldBuilder.addIntoInstance(player, instance, posInChunk00())

            chunkManager.onPlayerLeft(player)

            val neighbors = chunkManager.getNeighborChunkIds(ChunkId(0, 0))
            for (chunk in neighbors) {
                assertTrue(chunkManager.getChunkState(chunk) is ChunkState.ShuttingDown)
            }
        }

        @Test
        fun `chunk stays active if second player is still present`() {
            val player1 = objectBuilder.createPlayer()
            val player2 = objectBuilder.createPlayer()
            worldBuilder.addIntoInstance(player1, instance, posInChunk00())
            worldBuilder.addIntoInstance(player2, instance, posInChunk00Alt()) // same chunk (0,0), different cell

            chunkManager.onPlayerLeft(player1)

            // player2 still holds the chunk active
            assertEquals(ChunkState.Active, chunkManager.getChunkState(ChunkId(0, 0)))
        }

        @Test
        fun `shutdown timer ticks down and transitions to dormant`() {
            val shutdownMs = 1000
            val testGrid = GridBuilder(GridConstraintChecker()).buildGrid(10, 100, 100)
            val cm = ChunkManager(testGrid, 3, shutdownMs)

            val testInstance = worldBuilder.buildInstanceWithChunkManager(testGrid, cm)
            val player = objectBuilder.createPlayer()
            worldBuilder.addIntoInstance(player, testInstance, posInChunk00())

            cm.onPlayerLeft(player)
            val chunkId = ChunkId(0, 0)
            assertTrue(cm.getChunkState(chunkId) is ChunkState.ShuttingDown)

            // Tick 500ms — still shutting down
            val dormant1 = cm.update(500)
            assertTrue(dormant1.isEmpty())
            assertTrue(cm.getChunkState(chunkId) is ChunkState.ShuttingDown)

            // Tick 500ms more — now dormant
            val dormant2 = cm.update(500)
            assertTrue(dormant2.contains(chunkId))
            assertEquals(ChunkState.Dormant, cm.getChunkState(chunkId))
        }

        @Test
        fun `reactivating a shutting-down chunk cancels the shutdown`() {
            val shutdownMs = 5000
            val testGrid = GridBuilder(GridConstraintChecker()).buildGrid(10, 100, 100)
            val cm = ChunkManager(testGrid, 3, shutdownMs)

            val testInstance = worldBuilder.buildInstanceWithChunkManager(testGrid, cm)
            val player1 = objectBuilder.createPlayer()
            worldBuilder.addIntoInstance(player1, testInstance, posInChunk00())

            cm.onPlayerLeft(player1)
            assertTrue(cm.getChunkState(ChunkId(0, 0)) is ChunkState.ShuttingDown)

            // New player enters the same chunk area
            val player2 = objectBuilder.createPlayer()
            worldBuilder.addIntoInstance(player2, testInstance, posInChunk00())

            assertEquals(ChunkState.Active, cm.getChunkState(ChunkId(0, 0)))
        }
    }

    // ──────────────────────────────────────────────
    // Player counting
    // ──────────────────────────────────────────────

    @Nested
    inner class PlayerCounting {
        @Test
        fun `player count increments for chunk and all neighbors on enter`() {
            val player = objectBuilder.createPlayer()
            worldBuilder.addIntoInstance(player, instance, posInChunk00())

            val neighbors = chunkManager.getNeighborChunkIds(ChunkId(0, 0))
            for (chunk in neighbors) {
                assertEquals(1, chunkManager.getPlayerCount(chunk))
            }
        }

        @Test
        fun `player count decrements on leave`() {
            val player = objectBuilder.createPlayer()
            worldBuilder.addIntoInstance(player, instance, posInChunk00())

            chunkManager.onPlayerLeft(player)

            assertEquals(0, chunkManager.getPlayerCount(ChunkId(0, 0)))
        }

        @Test
        fun `overlapping neighbors accumulate player counts`() {
            val player1 = objectBuilder.createPlayer()
            val player2 = objectBuilder.createPlayer()

            // Both in adjacent chunks that share neighbor (1,1)
            worldBuilder.addIntoInstance(player1, instance, posInChunk00())   // chunk (0,0)
            worldBuilder.addIntoInstance(player2, instance, posInChunk11())   // chunk (1,1)

            // Chunk (1,1) is neighbor of both (0,0) and (1,1) → count = 2
            assertEquals(2, chunkManager.getPlayerCount(ChunkId(1, 1)))
        }
    }

    // ──────────────────────────────────────────────
    // Player chunk change
    // ──────────────────────────────────────────────

    @Nested
    inner class PlayerChunkChange {
        @Test
        fun `moving to a new chunk activates gained chunks and starts shutdown on lost chunks`() {
            val player = objectBuilder.createPlayer()
            worldBuilder.addIntoInstance(player, instance, posInChunk00())

            val oldChunkId = ChunkId(0, 0)
            val newChunkId = ChunkId(2, 2)

            chunkManager.onPlayerChunkChanged(oldChunkId, newChunkId)

            // New chunk's neighbors should be active
            val newNeighbors = chunkManager.getNeighborChunkIds(newChunkId)
            for (chunk in newNeighbors) {
                assertEquals(ChunkState.Active, chunkManager.getChunkState(chunk))
            }

            // Old exclusive neighbors should be shutting down
            val oldNeighbors = chunkManager.getNeighborChunkIds(oldChunkId)
            val lostChunks = oldNeighbors - newNeighbors.toSet()
            for (chunk in lostChunks) {
                assertTrue(chunkManager.getChunkState(chunk) is ChunkState.ShuttingDown)
            }
        }
    }

    // ──────────────────────────────────────────────
    // Entity registration & active entity iteration
    // ──────────────────────────────────────────────

    @Nested
    inner class EntityIteration {
        @Test
        fun `entities in active chunks are returned by getActiveEntitiesByType`() {
            val player = objectBuilder.createPlayer()
            worldBuilder.addIntoInstance(player, instance, posInChunk00())

            val unit = objectBuilder.createUnit()
            worldBuilder.addIntoInstance(unit, instance, posInChunk00Alt()) // same chunk area

            val activeGameObjects = chunkManager.getActiveEntitiesByType(ObjectGuid.GUID_TYPE.GAME_OBJECT).toList()
            assertTrue(activeGameObjects.contains(unit))
        }

        @Test
        fun `entities in dormant chunks are NOT returned by getActiveEntitiesByType`() {
            // No player → all chunks dormant
            val unit = objectBuilder.createUnit()
            worldBuilder.addIntoInstance(unit, instance, posInChunk00())

            val activeGameObjects = chunkManager.getActiveEntitiesByType(ObjectGuid.GUID_TYPE.GAME_OBJECT).toList()
            assertTrue(activeGameObjects.isEmpty())
        }

        @Test
        fun `entities in shutting-down chunks are still returned`() {
            val player = objectBuilder.createPlayer()
            worldBuilder.addIntoInstance(player, instance, posInChunk00())

            val unit = objectBuilder.createUnit()
            worldBuilder.addIntoInstance(unit, instance, posInChunk00Alt())

            // Player leaves → chunks start shutting down
            chunkManager.onPlayerLeft(player)

            val activeGameObjects = chunkManager.getActiveEntitiesByType(ObjectGuid.GUID_TYPE.GAME_OBJECT).toList()
            assertTrue(activeGameObjects.contains(unit))
        }

        @Test
        fun `unregistered entity is no longer returned`() {
            val player = objectBuilder.createPlayer()
            worldBuilder.addIntoInstance(player, instance, posInChunk00())

            val unit = objectBuilder.createUnit()
            worldBuilder.addIntoInstance(unit, instance, posInChunk00Alt())

            chunkManager.unregisterEntity(unit, unit.cachedChunkId!!)

            val activeGameObjects = chunkManager.getActiveEntitiesByType(ObjectGuid.GUID_TYPE.GAME_OBJECT).toList()
            assertFalse(activeGameObjects.contains(unit))
        }
    }

    // ──────────────────────────────────────────────
    // Despawn
    // ──────────────────────────────────────────────

    @Nested
    inner class Despawn {
        @Test
        fun `despawnChunkEntities removes non-player entities and marks them not in world`() {
            val player = objectBuilder.createPlayer()
            worldBuilder.addIntoInstance(player, instance, posInChunk00())

            val unit = objectBuilder.createUnit()
            worldBuilder.addIntoInstance(unit, instance, posInChunk00Alt())

            assertTrue(unit.isInWorld)

            val chunkId = chunkManager.getChunkIdForPosition(unit.position)
            val despawned = chunkManager.despawnChunkEntities(chunkId)

            assertTrue(despawned.contains(unit))
            assertFalse(unit.isInWorld)
        }

        @Test
        fun `despawnChunkEntities does not remove players`() {
            val player = objectBuilder.createPlayer()
            worldBuilder.addIntoInstance(player, instance, posInChunk00())

            val chunkId = chunkManager.getChunkIdForPosition(player.position)
            val despawned = chunkManager.despawnChunkEntities(chunkId)

            assertFalse(despawned.contains(player))
            assertTrue(player.isInWorld)
        }

        @Test
        fun `despawned entities are removed from the grid`() {
            val player = objectBuilder.createPlayer()
            worldBuilder.addIntoInstance(player, instance, posInChunk00())

            val unit = objectBuilder.createUnit()
            worldBuilder.addIntoInstance(unit, instance, posInChunk00Alt())

            val chunkId = chunkManager.getChunkIdForPosition(unit.position)
            chunkManager.despawnChunkEntities(chunkId)

            val found = instance.grid.findObjectByGuid(unit.guid)
            assertEquals(null, found)
        }
    }
}
