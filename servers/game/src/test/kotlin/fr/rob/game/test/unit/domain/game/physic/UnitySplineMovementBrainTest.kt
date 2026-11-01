package fr.rob.game.test.unit.domain.game.physic

import fr.raven.log.NullLogger
import fr.raven.proto.message.physicbridge.PhysicProto
import fr.raven.proto.message.physicbridge.PhysicProto.ObjectMoved
import fr.raven.proto.message.physicbridge.PhysicProto.ObjectReachDestination
import fr.rob.core.network.v2.session.Session
import fr.rob.core.network.v2.session.SessionSocketInterface
import fr.rob.game.component.MovementComponent
import fr.rob.game.config.PhysicConfig
import fr.rob.game.entity.Position
import fr.rob.game.entity.WorldObject
import fr.rob.game.entity.guid.ObjectGuidGenerator
import fr.rob.game.entity.movement.spline.SplineStep
import fr.rob.game.entity.movement.spline.UnitySplineMovementBrain
import fr.rob.game.physic.ObjectMovedHandler
import fr.rob.game.physic.ObjectReachedDestinationHandler
import fr.rob.game.physic.PhysicFunctionParameters
import fr.rob.game.physic.PhysicObjectInteraction
import fr.rob.game.physic.PhysicOpcodeFunctionRegistry
import fr.rob.game.physic.unity.UnityIntegration
import fr.rob.game.test.unit.tools.DummyWorldObjectBuilder
import fr.rob.game.test.unit.tools.WorldBuilder
import fr.rob.game.world.WorldTaskQueue
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import java.net.ServerSocket
import java.net.Socket
import kotlin.concurrent.thread
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class UnitySplineMovementBrainTest {
    private val physicServer = ServerSocket(0)
    private val physicClients = mutableListOf<Socket>()
    private val integration = UnityIntegration(
        PhysicConfig("127.0.0.1", physicServer.localPort, connectTimeoutMs = 500, retryDelayMs = 0),
        PhysicOpcodeFunctionRegistry(arrayOf()),
        NullLogger(),
    )
    private val worldTaskQueue = WorldTaskQueue()
    private val interaction = PhysicObjectInteraction()
    private val brain = UnitySplineMovementBrain(integration, interaction, worldTaskQueue)
    private val movedHandler = ObjectMovedHandler(interaction, worldTaskQueue)
    private val reachedHandler = ObjectReachedDestinationHandler(interaction, worldTaskQueue)

    private val worldBuilder = WorldBuilder()
    private val source: WorldObject = DummyWorldObjectBuilder(ObjectGuidGenerator()).createUnit().also {
        worldBuilder.addIntoInstance(it, worldBuilder.buildBasicWorld(), Position(0f, 0f, 0f, 0f))
    }
    private val steps = mutableListOf<SplineStep>()

    init {
        thread(isDaemon = true) {
            while (!physicServer.isClosed) {
                val socket = runCatching { physicServer.accept() }.getOrNull() ?: return@thread
                synchronized(physicClients) { physicClients.add(socket) }
            }
        }

        integration.connect()
        awaitUntil { integration.isConnected() }
    }

    @AfterEach
    fun tearDown() {
        integration.close()
        physicServer.close()
    }

    @Test
    fun `physic messages are applied on the world thread only, each as its own step`() {
        brain.moveToDestination(source, Position(10f, 0f, 0f, 0f)) { steps.add(it) }

        movedHandler.callForMessage(moved(1f), parameters())
        movedHandler.callForMessage(moved(2f), parameters())

        assertTrue(steps.isEmpty(), "nothing must run on the physic thread")

        worldTaskQueue.dequeue()

        assertEquals(listOf(1f, 2f), steps.map { it.position.x })
        assertTrue(steps.none { it.reachedDestination })
    }

    @Test
    fun `reaching the destination stops the spline and ignores later messages`() {
        brain.moveToDestination(source, Position(10f, 0f, 0f, 0f)) { steps.add(it) }

        reachedHandler.callForMessage(ObjectReachDestination.newBuilder().setGuid(source.guid.getRawValue()).build(), parameters())
        movedHandler.callForMessage(moved(3f), parameters())
        worldTaskQueue.dequeue()

        assertEquals(1, steps.size)
        assertTrue(steps.single().reachedDestination)
        assertEquals(MovementComponent.Phase.STOPPED, steps.single().movement.phase)
    }

    @Test
    fun `a new spline replaces the previous one of the same object`() {
        val previousSteps = mutableListOf<SplineStep>()
        brain.moveToDestination(source, Position(10f, 0f, 0f, 0f)) { previousSteps.add(it) }
        brain.moveToDestination(source, Position(20f, 0f, 0f, 0f)) { steps.add(it) }

        movedHandler.callForMessage(moved(1f), parameters())
        worldTaskQueue.dequeue()

        assertTrue(previousSteps.isEmpty())
        assertEquals(1, steps.size)
    }

    @Test
    fun `connection lost stops active splines on the world thread`() {
        brain.moveToDestination(source, Position(10f, 0f, 0f, 0f)) { steps.add(it) }

        synchronized(physicClients) { physicClients.forEach { it.close() } }
        awaitUntil { !integration.isConnected() }
        Thread.sleep(50) // let the connection lost listener enqueue its task

        assertTrue(steps.isEmpty())

        worldTaskQueue.dequeue()

        assertEquals(1, steps.size)
        assertTrue(steps.single().reachedDestination)
        assertEquals(source.position, steps.single().position)

        movedHandler.callForMessage(moved(4f), parameters())
        worldTaskQueue.dequeue()
        assertEquals(1, steps.size)
    }

    private fun moved(x: Float): ObjectMoved =
        ObjectMoved.newBuilder()
            .setGuid(source.guid.getRawValue())
            .setPosition(PhysicProto.Position.newBuilder().setPosX(x))
            .setDirection(PhysicProto.Vec3.newBuilder().setX(1f))
            .build()

    private fun parameters() = PhysicFunctionParameters(
        0,
        PhysicProto.Packet.getDefaultInstance(),
        Session(
            object : SessionSocketInterface {
                override fun getIp(): String = "127.0.0.1"
                override fun send(data: Any) {}
                override fun close() {}
            },
        ),
    )

    private fun awaitUntil(condition: () -> Boolean) {
        val deadline = System.currentTimeMillis() + 2_000

        while (!condition()) {
            check(System.currentTimeMillis() < deadline) { "Condition not met in time" }
            Thread.sleep(10)
        }
    }
}
