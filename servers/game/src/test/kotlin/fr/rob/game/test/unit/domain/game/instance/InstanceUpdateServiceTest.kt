package fr.rob.game.test.unit.domain.game.instance

import fr.rob.game.behavior.BehaviorInterface
import fr.rob.game.entity.Position
import fr.rob.game.entity.ScriptedWorldObject
import fr.rob.game.entity.WorldObject
import fr.rob.game.entity.guid.ObjectGuid
import fr.rob.game.entity.guid.ObjectGuidGenerator
import fr.rob.game.event.ListEventDispatcher
import fr.rob.game.instance.InstanceManager
import fr.rob.game.instance.InstanceUpdateService
import fr.rob.game.instance.MapInstance
import fr.rob.game.map.grid.GridBuilder
import fr.rob.game.map.grid.GridConstraintChecker
import fr.rob.game.map.grid.chunk.ChunkId
import fr.rob.game.map.grid.chunk.ChunkManager
import fr.rob.game.map.grid.chunk.ChunkState
import fr.rob.game.map.grid.chunk.ChunkTransitionListener
import fr.rob.game.test.unit.tools.DummyWorldObjectBuilder
import fr.rob.game.test.unit.tools.VoidEventDispatcher
import fr.rob.game.test.unit.tools.WorldBuilder
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

/**
 * Integration tests for [InstanceUpdateService] — verifies the full tick
 * orchestration: chunk-aware entity updates, scriptable object updates,
 * despawn on shutdown expiry, and scheduled removal processing.
 *
 * Uses the same grid layout as ChunkManagerTest (10x10 cells, cellSize=10, zone 100x100).
 */
class InstanceUpdateServiceTest {

    private val worldBuilder = WorldBuilder()
    private val instanceManager = createTestInstanceManager()
    private val service = InstanceUpdateService(instanceManager)
    private val eventDispatcher = VoidEventDispatcher()
    private val objectBuilder = DummyWorldObjectBuilder(ObjectGuidGenerator())
    private val guidGenerator = ObjectGuidGenerator()

    companion object {
        /** Cell (0,0) → chunk (0,0) */
        fun posInChunk00() = Position(-45f, -45f, 0f, 0f)

        /** Cell (1,0) → chunk (0,0) — same chunk, different cell */
        fun posInChunk00Alt() = Position(-35f, -45f, 0f, 0f)

        /** Cell (9,9) → chunk (3,3) — far away from chunk (0,0) */
        fun posInChunk33() = Position(45f, 45f, 0f, 0f)

        /** Cell (3,0) → chunk (1,0) */
        fun posInChunk10() = Position(-15f, -45f, 0f, 0f)

        private fun createTestInstanceManager(): InstanceManager {
            return InstanceManager(GridBuilder(GridConstraintChecker()))
        }
    }

    /**
     * Builds an instance using WorldBuilder's basic world and registers its
     * chunk manager in the test InstanceManager.
     */
    private fun buildTestInstance(): MapInstance {
        val instance = worldBuilder.buildBasicWorld()
        val chunkManager = worldBuilder.getChunkManager(instance.id)!!
        instanceManager.registerChunkManager(instance.id, chunkManager)
        return instance
    }

    private fun buildCustomInstance(
        grid: fr.rob.game.map.grid.Grid,
        cm: ChunkManager,
    ): MapInstance {
        val instance = worldBuilder.buildInstanceWithChunkManager(grid, cm)
        instanceManager.registerChunkManager(instance.id, cm)
        return instance
    }

    // ──────────────────────────────────────────────
    // Chunk-aware entity ticking
    // ──────────────────────────────────────────────

    @Nested
    inner class ChunkAwareTicking {
        @Test
        fun `entities in active chunks are ticked`() {
            val instance = buildTestInstance()
            val player = objectBuilder.createPlayer()
            worldBuilder.addIntoInstance(player, instance, posInChunk00())

            val unit = objectBuilder.createUnit()
            val spy = TickCountBehavior()
            unit.addBehavior(spy)
            worldBuilder.addIntoInstance(unit, instance, posInChunk00Alt())

            service.update(instance, 20, eventDispatcher)

            assertEquals(1, spy.tickCount)
        }

        @Test
        fun `entities in dormant chunks are NOT ticked`() {
            val instance = buildTestInstance()

            // No player → all chunks dormant
            val unit = objectBuilder.createUnit()
            val spy = TickCountBehavior()
            unit.addBehavior(spy)
            worldBuilder.addIntoInstance(unit, instance, posInChunk00())

            service.update(instance, 20, eventDispatcher)

            assertEquals(0, spy.tickCount)
        }

        @Test
        fun `entities in shutting-down chunks are still ticked`() {
            val instance = buildTestInstance()
            val player = objectBuilder.createPlayer()
            worldBuilder.addIntoInstance(player, instance, posInChunk00())

            val unit = objectBuilder.createUnit()
            val spy = TickCountBehavior()
            unit.addBehavior(spy)
            worldBuilder.addIntoInstance(unit, instance, posInChunk00Alt())

            val cm = instanceManager.getChunkManager(instance.id)
            cm.onPlayerLeft(player)

            service.update(instance, 20, eventDispatcher)

            assertEquals(1, spy.tickCount)
        }

        @Test
        fun `players in active chunks are ticked`() {
            val instance = buildTestInstance()
            val player = objectBuilder.createPlayer()
            val spy = TickCountBehavior()
            player.addBehavior(spy)
            worldBuilder.addIntoInstance(player, instance, posInChunk00())

            service.update(instance, 20, eventDispatcher)

            assertEquals(1, spy.tickCount)
        }
    }

    // ──────────────────────────────────────────────
    // ScriptedWorldObjects always ticked
    // ──────────────────────────────────────────────

    @Nested
    inner class ScriptedWorldObjectTicking {
        @Test
        fun `scripted world objects are ticked even with all chunks dormant`() {
            val instance = buildTestInstance()

            val scripted = createScriptedWorldObject()
            val spy = TickCountBehavior()
            scripted.addBehavior(spy)
            worldBuilder.addIntoInstance(scripted, instance, posInChunk00())

            // No player → all chunks dormant
            service.update(instance, 20, eventDispatcher)

            assertEquals(1, spy.tickCount)
        }

        @Test
        fun `scripted world objects are ticked alongside active chunk entities`() {
            val instance = buildTestInstance()
            val player = objectBuilder.createPlayer()
            worldBuilder.addIntoInstance(player, instance, posInChunk00())

            val scripted = createScriptedWorldObject()
            val spyScripted = TickCountBehavior()
            scripted.addBehavior(spyScripted)
            worldBuilder.addIntoInstance(scripted, instance, posInChunk33()) // far away chunk, dormant

            val unit = objectBuilder.createUnit()
            val spyUnit = TickCountBehavior()
            unit.addBehavior(spyUnit)
            worldBuilder.addIntoInstance(unit, instance, posInChunk00Alt())

            service.update(instance, 20, eventDispatcher)

            assertEquals(1, spyScripted.tickCount, "ScriptedWorldObject should be ticked regardless of chunk state")
            assertEquals(1, spyUnit.tickCount, "GAME_OBJECT in active chunk should be ticked")
        }
    }

    // ──────────────────────────────────────────────
    // Despawn on shutdown expiry
    // ──────────────────────────────────────────────

    @Nested
    inner class DespawnOnShutdownExpiry {
        @Test
        fun `entities are despawned when shutdown timer expires during update`() {
            val shutdownMs = 100
            val grid = GridBuilder(GridConstraintChecker()).buildGrid(10, 100, 100)
            val cm = ChunkManager(grid, 3, shutdownMs)
            val instance = buildCustomInstance(grid, cm)

            val player = objectBuilder.createPlayer()
            worldBuilder.addIntoInstance(player, instance, posInChunk00())

            val unit = objectBuilder.createUnit()
            worldBuilder.addIntoInstance(unit, instance, posInChunk00Alt())

            cm.onPlayerLeft(player)
            assertTrue(unit.isInWorld)

            // Tick enough to expire the shutdown
            service.update(instance, 100, eventDispatcher)

            val chunkId = cm.getChunkIdForPosition(unit.position)
            assertEquals(ChunkState.Dormant, cm.getChunkState(chunkId))
            assertFalse(unit.isInWorld)
        }
    }

    // ──────────────────────────────────────────────
    // Scheduled removal processing
    // ──────────────────────────────────────────────

    @Nested
    inner class ScheduledRemovalProcessing {
        @Test
        fun `scheduled removals are processed and entities removed from grid`() {
            val instance = buildTestInstance()
            val player = objectBuilder.createPlayer()
            worldBuilder.addIntoInstance(player, instance, posInChunk00())

            val unit = objectBuilder.createUnit()
            worldBuilder.addIntoInstance(unit, instance, posInChunk00Alt())

            unit.scheduleRemoveFromInstance()

            service.update(instance, 20, eventDispatcher)

            val found = instance.grid.findObjectByGuid(unit.guid)
            assertEquals(null, found)
        }

        @Test
        fun `player removal triggers onPlayerLeft on chunk manager`() {
            val instance = buildTestInstance()
            val player = objectBuilder.createPlayer()
            worldBuilder.addIntoInstance(player, instance, posInChunk00())

            val cm = instanceManager.getChunkManager(instance.id)
            val chunkId = ChunkId(0, 0)
            assertEquals(1, cm.getPlayerCount(chunkId))

            player.scheduleRemoveFromInstance()
            service.update(instance, 20, eventDispatcher)

            assertEquals(0, cm.getPlayerCount(chunkId))
        }
    }

    // ──────────────────────────────────────────────
    // Full lifecycle
    // ──────────────────────────────────────────────

    @Nested
    inner class FullLifecycle {
        @Test
        fun `multiple ticks with player enter, entity tick, player leave, shutdown, and despawn`() {
            val shutdownMs = 200
            val grid = GridBuilder(GridConstraintChecker()).buildGrid(10, 100, 100)
            val cm = ChunkManager(grid, 3, shutdownMs)
            val instance = buildCustomInstance(grid, cm)

            val player = objectBuilder.createPlayer()
            worldBuilder.addIntoInstance(player, instance, posInChunk00())

            val unit = objectBuilder.createUnit()
            val spy = TickCountBehavior()
            unit.addBehavior(spy)
            worldBuilder.addIntoInstance(unit, instance, posInChunk00Alt())

            // Tick 1: everything active
            service.update(instance, 50, eventDispatcher)
            assertEquals(1, spy.tickCount)
            assertTrue(unit.isInWorld)

            // Player leaves → shutdown timer starts at 200ms
            cm.onPlayerLeft(player)

            // Tick 2: 200 → 150ms, still shutting down, entities ticked
            service.update(instance, 50, eventDispatcher)
            assertEquals(2, spy.tickCount)

            // Tick 3: 150 → 100ms
            service.update(instance, 50, eventDispatcher)
            assertEquals(3, spy.tickCount)

            // Tick 4: 100 → 50ms
            service.update(instance, 50, eventDispatcher)
            assertEquals(4, spy.tickCount)
            assertTrue(unit.isInWorld)

            // Tick 5: 50 → 0ms, shutdown expires → chunk becomes dormant → entities despawned
            service.update(instance, 50, eventDispatcher)
            assertFalse(unit.isInWorld)
        }
    }

    // ──────────────────────────────────────────────
    // Chunk transition on movement
    // ──────────────────────────────────────────────

    @Nested
    inner class ChunkTransitionOnMovement {

        private fun buildEventDispatcherWithChunkTransition(): ListEventDispatcher {
            val dispatcher = ListEventDispatcher()
            dispatcher.attachListener(ChunkTransitionListener(instanceManager))
            return dispatcher
        }

        @Test
        fun `player moving to another chunk is transferred from old chunk entity set to new chunk entity set`() {
            val instance = buildTestInstance()
            val cm = instanceManager.getChunkManager(instance.id)
            val dispatcher = buildEventDispatcherWithChunkTransition()

            val player = objectBuilder.createPlayer()
            worldBuilder.addIntoInstance(player, instance, posInChunk00())
            assertEquals(ChunkId(0, 0), player.cachedChunkId)

            player.addBehavior(MoveOnUpdateBehavior(posInChunk33()))
            service.update(instance, 20, dispatcher)

            assertEquals(ChunkId(3, 3), player.cachedChunkId)

            val playersInNewChunk = cm.getActiveEntitiesByType(ObjectGuid.GUID_TYPE.PLAYER).toList()
            assertTrue(playersInNewChunk.contains(player))
            assertEquals(ChunkState.Active, cm.getChunkState(ChunkId(3, 3)))
        }

        @Test
        fun `game object moving to another chunk is transferred between chunk entity sets`() {
            val instance = buildTestInstance()
            val cm = instanceManager.getChunkManager(instance.id)
            val dispatcher = buildEventDispatcherWithChunkTransition()

            val player = objectBuilder.createPlayer()
            worldBuilder.addIntoInstance(player, instance, posInChunk00())

            val unit = objectBuilder.createUnit()
            worldBuilder.addIntoInstance(unit, instance, posInChunk00Alt())
            assertEquals(ChunkId(0, 0), unit.cachedChunkId)

            unit.addBehavior(MoveOnUpdateBehavior(posInChunk10()))
            service.update(instance, 20, dispatcher)

            assertEquals(ChunkId(1, 0), unit.cachedChunkId)

            val activeUnits = cm.getActiveEntitiesByType(ObjectGuid.GUID_TYPE.GAME_OBJECT).toList()
            assertTrue(activeUnits.contains(unit))
        }
    }

    // ──────────────────────────────────────────────
    // Helpers
    // ──────────────────────────────────────────────

    private fun createScriptedWorldObject(): ScriptedWorldObject {
        val guid = guidGenerator.createForScriptableObject(
            objectBuilder.createUnit(),
            1,
        )
        return ScriptedWorldObject(guid)
    }

    private class TickCountBehavior : BehaviorInterface {
        var tickCount = 0

        override fun update(worldObject: WorldObject, deltaTime: Int) {
            tickCount++
        }

        override fun canApplyTo(worldObject: WorldObject): Boolean = true
    }

    private class MoveOnUpdateBehavior(private val target: Position) : BehaviorInterface {
        private var moved = false

        override fun update(worldObject: WorldObject, deltaTime: Int) {
            if (!moved) {
                worldObject.setPosition(target.x, target.y, target.z, target.orientation)
                moved = true
            }
        }

        override fun canApplyTo(worldObject: WorldObject): Boolean = true
    }
}
