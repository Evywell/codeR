package fr.rob.game.test.unit.domain.game.physic

import fr.raven.log.NullLogger
import fr.raven.proto.message.physicbridge.PhysicProto.Packet
import fr.rob.game.config.PhysicConfig
import fr.rob.game.entity.movement.spline.exception.ActionNotHandledByBrainException
import fr.rob.game.physic.PhysicOpcodeFunctionRegistry
import fr.rob.game.physic.unity.UnityIntegration
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.DataInputStream
import java.net.ServerSocket
import java.net.Socket
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import kotlin.concurrent.thread
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class UnityIntegrationTest {
    private val closeables = mutableListOf<AutoCloseable>()

    @AfterEach
    fun tearDown() {
        closeables.reversed().forEach { it.close() }
    }

    @Test
    fun `send without physic server does not block and reports the failed attempt`() {
        val integration = integration(freePort(), retryDelayMs = 60_000)
        val connectionLost = AtomicInteger()
        integration.addConnectionLostListener { connectionLost.incrementAndGet() }

        val startedAt = System.nanoTime()
        integration.send(packet(1))
        val elapsedMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startedAt)

        assertTrue(elapsedMs < 200, "send blocked for ${elapsedMs}ms")
        awaitUntil { connectionLost.get() == 1 }
        assertFalse(integration.isConnected())

        // Retry delay not over: nothing to keep the packet for
        assertThrows<ActionNotHandledByBrainException> { integration.send(packet(2)) }
    }

    @Test
    fun `packets sent while connecting are kept and arrive in order`() {
        val server = FakePhysicServer()
        val integration = integration(server.port)

        (1..5).forEach { integration.send(packet(it)) }
        awaitUntil { integration.isConnected() }
        (6..8).forEach { integration.send(packet(it)) }

        assertEquals((1..8).toList(), server.receive(8))
    }

    @Test
    fun `connect at startup lets the first send go to the physic server`() {
        val server = FakePhysicServer()
        val integration = integration(server.port)

        integration.connect()
        awaitUntil { integration.isConnected() }

        integration.send(packet(1))
        assertEquals(listOf(1), server.receive(1))
    }

    @Test
    fun `connection lost is reported and the next send reconnects`() {
        val server = FakePhysicServer()
        val integration = integration(server.port)
        val connectionLost = AtomicInteger()
        integration.addConnectionLostListener { connectionLost.incrementAndGet() }

        integration.connect()
        awaitUntil { integration.isConnected() }

        server.closeClients()
        awaitUntil { connectionLost.get() == 1 }
        assertFalse(integration.isConnected())

        integration.send(packet(7))
        assertEquals(listOf(7), server.receive(1))
    }

    @Test
    fun `failed attempt waits for the retry delay before the next one`() {
        var now = 0L
        val port = freePort()
        val integration = integration(port, retryDelayMs = 1_000) { now }
        val connectionLost = AtomicInteger()
        integration.addConnectionLostListener { connectionLost.incrementAndGet() }

        integration.connect()
        awaitUntil { connectionLost.get() == 1 }

        val server = FakePhysicServer(port)

        assertThrows<ActionNotHandledByBrainException> { integration.send(packet(0)) }

        now = 1_000
        integration.send(packet(3))
        assertEquals(listOf(3), server.receive(1))
    }

    private fun integration(port: Int, retryDelayMs: Long = 0, currentTimeMillis: () -> Long = System::currentTimeMillis) =
        UnityIntegration(
            PhysicConfig("127.0.0.1", port, connectTimeoutMs = 500, retryDelayMs = retryDelayMs),
            PhysicOpcodeFunctionRegistry(arrayOf()),
            NullLogger(),
            currentTimeMillis,
        ).also { closeables.add(it) }

    private fun freePort(): Int = ServerSocket(0).use { it.localPort }

    private fun packet(opcode: Int): Packet = Packet.newBuilder().setOpcode(opcode).build()

    private fun awaitUntil(condition: () -> Boolean) {
        val deadline = System.currentTimeMillis() + 2_000

        while (!condition()) {
            check(System.currentTimeMillis() < deadline) { "Condition not met in time" }
            Thread.sleep(10)
        }
    }

    /** Reads length-prefixed [Packet]s, like the Unity physic server. */
    private inner class FakePhysicServer(port: Int = 0) : AutoCloseable {
        private val serverSocket = ServerSocket(port)
        private val clients = mutableListOf<Socket>()
        private val received = LinkedBlockingQueue<Packet>()
        val port: Int = serverSocket.localPort

        init {
            closeables.add(this)

            thread(isDaemon = true) {
                while (!serverSocket.isClosed) {
                    val socket = runCatching { serverSocket.accept() }.getOrNull() ?: return@thread
                    synchronized(clients) { clients.add(socket) }

                    thread(isDaemon = true) {
                        val input = DataInputStream(socket.getInputStream())
                        runCatching {
                            while (true) {
                                val bytes = ByteArray(input.readInt())
                                input.readFully(bytes)
                                received.add(Packet.parseFrom(bytes))
                            }
                        }
                    }
                }
            }
        }

        fun receive(count: Int): List<Int> = (1..count).map {
            checkNotNull(received.poll(2, TimeUnit.SECONDS)) { "Packet $it of $count not received" }.opcode
        }

        fun closeClients() = synchronized(clients) { clients.forEach { it.close() } }

        override fun close() {
            closeClients()
            serverSocket.close()
        }
    }
}
