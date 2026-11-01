package fr.rob.game.test.unit.domain.game.world.entity.visibility

import fr.rob.game.entity.Position
import fr.rob.game.entity.WorldObject
import fr.rob.game.entity.guid.ObjectGuid
import fr.rob.game.entity.guid.ObjectGuidGenerator
import fr.rob.game.entity.notifier.WorldObjectUpdatedNotifier
import fr.rob.game.entity.visibility.VisibilityService
import fr.rob.game.instance.MapInstance
import fr.rob.game.map.grid.GridBuilder
import fr.rob.game.map.grid.GridConstraintChecker
import fr.rob.game.map.grid.chunk.ChunkManager
import fr.rob.game.player.Player
import fr.rob.game.player.message.MovementHeartbeatMessage
import fr.rob.game.player.message.NearbyObjectMessage
import fr.rob.game.player.message.ObjectDescriptionMessage
import fr.rob.game.player.message.ObjectDestroyMessage
import fr.rob.game.player.session.GameSession
import fr.rob.game.test.unit.sandbox.network.session.StoreMessageSender
import fr.rob.game.test.unit.tools.DummyWorldObjectBuilder
import fr.rob.game.test.unit.tools.WorldBuilder
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class VisibilityServiceTest {
    private val worldBuilder = WorldBuilder()
    private val guidGenerator = ObjectGuidGenerator()
    private val objectBuilder = DummyWorldObjectBuilder(guidGenerator)
    private val service = VisibilityService()

    // 400x400 zone centered on 0, so objects can be far beyond the leave radius
    private val instance: MapInstance = run {
        val grid = GridBuilder(GridConstraintChecker()).buildGrid(10, 400, 400)
        worldBuilder.buildInstanceWithChunkManager(grid, ChunkManager(grid, ChunkManager.DEFAULT_CHUNK_SIZE, ChunkManager.DEFAULT_SHUTDOWN_DURATION_MS))
    }

    private class Client(val player: Player, val sender: StoreMessageSender) {
        fun created(): List<ObjectGuid> = bodies<NearbyObjectMessage>().map { it.objectId }

        fun described(): List<ObjectGuid> = bodies<ObjectDescriptionMessage>().map { it.worldObject.guid }

        fun destroyed(): List<ObjectGuid> = bodies<ObjectDestroyMessage>().map { it.objectId }

        fun heartbeats(): List<ObjectGuid> = bodies<MovementHeartbeatMessage>().map { it.objectId }

        inline fun <reified T> bodies(): List<T> = sender.getMessages().map { it.message.body }.filterIsInstance<T>()
    }

    private var nextPlayerId = 1

    private fun player(position: Position): Client {
        val sender = StoreMessageSender()
        val id = nextPlayerId++
        val player = Player(GameSession(id, sender), guidGenerator.createForPlayer(id), "Player $id", 1)
        worldBuilder.addIntoInstance(player, instance, position)

        return Client(player, sender)
    }

    private fun unit(position: Position): WorldObject = objectBuilder.createUnit().also { worldBuilder.addIntoInstance(it, instance, position) }

    private fun tick(deltaTime: Int = 0) = service.update(instance, deltaTime)

    @Test
    fun `a player entering the world receives itself and the objects in range`() {
        val near = unit(Position(30f, 0f, 0f, 0f))
        val far = unit(Position(55f, 0f, 0f, 0f))
        val client = player(Position(0f, 0f, 0f, 0f))

        tick()

        assertEquals(listOf(client.player.guid, near.guid), client.created())
        assertEquals(listOf(near.guid), client.described())
        assertTrue(far.guid !in client.created())
    }

    @Test
    fun `objects are created once`() {
        val near = unit(Position(30f, 0f, 0f, 0f))
        val client = player(Position(0f, 0f, 0f, 0f))

        tick()
        tick(VisibilityService.REFRESH_INTERVAL_MS)

        assertEquals(1, client.created().count { it == near.guid })
    }

    @Test
    fun `an object stays visible between the enter and leave radius, then is destroyed beyond`() {
        val mob = unit(Position(40f, 0f, 0f, 0f))
        val client = player(Position(0f, 0f, 0f, 0f))
        tick()

        mob.setPosition(55f, 0f, 0f, 0f)
        tick(VisibilityService.REFRESH_INTERVAL_MS)
        assertTrue(client.destroyed().isEmpty())

        mob.setPosition(65f, 0f, 0f, 0f)
        tick(VisibilityService.REFRESH_INTERVAL_MS)
        assertEquals(listOf(mob.guid), client.destroyed())
    }

    @Test
    fun `an object coming back is only created again inside the enter radius`() {
        val mob = unit(Position(70f, 0f, 0f, 0f))
        val client = player(Position(0f, 0f, 0f, 0f))
        tick()

        mob.setPosition(55f, 0f, 0f, 0f)
        tick(VisibilityService.REFRESH_INTERVAL_MS)
        assertTrue(mob.guid !in client.created())

        mob.setPosition(45f, 0f, 0f, 0f)
        tick(VisibilityService.REFRESH_INTERVAL_MS)
        assertTrue(mob.guid in client.created())
    }

    @Test
    fun `a removed object is destroyed`() {
        val mob = unit(Position(10f, 0f, 0f, 0f))
        val client = player(Position(0f, 0f, 0f, 0f))
        tick()

        instance.grid.removeWorldObject(mob)
        tick(VisibilityService.REFRESH_INTERVAL_MS)

        assertEquals(listOf(mob.guid), client.destroyed())
    }

    @Test
    fun `a player is refreshed when it changes cell, without waiting for the interval`() {
        val mob = unit(Position(100f, 0f, 0f, 0f))
        val client = player(Position(0f, 0f, 0f, 0f))
        tick()

        client.player.setPosition(60f, 0f, 0f, 0f)
        tick()

        assertTrue(mob.guid in client.created())
    }

    @Test
    fun `an object moving into range of a still player is seen at the next interval`() {
        val mob = unit(Position(100f, 0f, 0f, 0f))
        val client = player(Position(0f, 0f, 0f, 0f))
        tick()

        mob.setPosition(20f, 0f, 0f, 0f)
        tick(VisibilityService.REFRESH_INTERVAL_MS - 1)
        assertTrue(mob.guid !in client.created())

        tick(1)
        assertTrue(mob.guid in client.created())
    }

    @Test
    fun `heartbeats only go to players that see the object, and to the mover itself`() {
        val mob = unit(Position(0f, 0f, 0f, 0f))
        val observer = player(Position(30f, 0f, 0f, 0f))
        val stranger = player(Position(150f, 0f, 0f, 0f))
        tick()

        WorldObjectUpdatedNotifier(service).visit(mob)
        WorldObjectUpdatedNotifier(service).visit(observer.player)

        assertEquals(listOf(mob.guid, observer.player.guid), observer.heartbeats())
        assertTrue(stranger.heartbeats().isEmpty())
    }
}
