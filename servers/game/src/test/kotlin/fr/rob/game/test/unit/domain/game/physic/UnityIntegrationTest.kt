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
    fun `send without physic server throws without blocking`() {
        val port = ServerSocket(0).use { it.localPort }
        val integration = integration(port)

        val startedAt = System.nanoTime()
        assertThrows<ActionNotHandledByBrainException> { integration.send(packet(1)) }
        val elapsedMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startedAt)

        assertTrue(elapsedMs < 200, "send blocked for ${elapsedMs}ms")
        assertFalse(integration.isConnected())
    }

    @Test
    fun `first send opens the connection and later packets arrive in order`() {
        val server = FakePhysicServer()
        val integration = integration(server.port)

        assertThrows<ActionNotHandledByBrainException> { integration.send(packet(0)) }
        awaitUntil { integration.isConnected() }

        (1..5).forEach { integration.send(packet(it)) }

        assertEquals((1..5).toList(), (1..5).map { server.received.poll(2, TimeUnit.SECONDS)!!.opcode })
    }

    @Test
    fun `connect at startup lets the first send go to the physic server`() {
        val server = FakePhysicServer()
        val integration = integration(server.port)

        integration.connect()
        awaitUntil { integration.isConnected() }

        integration.send(packet(1))
        assertEquals(1, server.received.poll(2, TimeUnit.SECONDS)!!.opcode)
    }

    @Test
    fun `connection lost is detected and the next send reconnects`() {
        val server = FakePhysicServer()
        val integration = integration(server.port)

        assertThrows<ActionNotHandledByBrainException> { integration.send(packet(0)) }
        awaitUntil { integration.isConnected() }

        server.closeClients()
        awaitUntil { !integration.isConnected() }

        assertThrows<ActionNotHandledByBrainException> { integration.send(packet(0)) }
        awaitUntil { integration.isConnected() }

        integration.send(packet(7))
        assertEquals(7, server.received.poll(2, TimeUnit.SECONDS)!!.opcode)
    }

    @Test
    fun `failed attempt waits for the retry delay before the next one`() {
        var now = 0L
        val port = ServerSocket(0).use { it.localPort }
        val integration = integration(port, retryDelayMs = 1_000) { now }

        assertThrows<ActionNotHandledByBrainException> { integration.send(packet(0)) }
        Thread.sleep(300) // let the refused connection complete

        val server = FakePhysicServer(port)

        assertThrows<ActionNotHandledByBrainException> { integration.send(packet(0)) }
        Thread.sleep(300)
        assertFalse(integration.isConnected())

        now = 1_000
        assertThrows<ActionNotHandledByBrainException> { integration.send(packet(0)) }
        awaitUntil { integration.isConnected() }

        integration.send(packet(3))
        assertEquals(3, server.received.poll(2, TimeUnit.SECONDS)!!.opcode)
    }

    private fun integration(port: Int, retryDelayMs: Long = 0, currentTimeMillis: () -> Long = System::currentTimeMillis) =
        UnityIntegration(
            PhysicConfig("127.0.0.1", port, connectTimeoutMs = 500, retryDelayMs = retryDelayMs),
            PhysicOpcodeFunctionRegistry(arrayOf()),
            NullLogger(),
            currentTimeMillis,
        ).also { closeables.add(it) }

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
        val port: Int = serverSocket.localPort
        val received = LinkedBlockingQueue<Packet>()

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

        fun closeClients() = synchronized(clients) { clients.forEach { it.close() } }

        override fun close() {
            closeClients()
            serverSocket.close()
        }
    }
}
