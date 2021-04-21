package fr.rob.login.test.feature

import com.nhaarman.mockitokotlin2.mock
import fr.rob.core.ENV_DEV
import fr.rob.core.network.Packet
import fr.rob.login.test.feature.service.exception.TimeoutException
import fr.rob.login.test.feature.service.network.Client
import org.junit.After
import org.junit.Before
import java.io.File

open class Scenario {

    private val appServer = LoginApplication(ENV_DEV)
    private val appClient = ClientApplication()

    val client = Client(appClient)

    @Before
    fun setUp() {
        val configFileMock = mock<File>()

        appServer.loadConfig(configFileMock)
        appServer.run()

        client.connectTo(appServer.server)
    }

    @After
    fun cleanTest() {
        appServer.stop()
        client.reset()

        while (!appServer.server.isShutdownSuccessfully || !client.isShutdownSuccessfully) {
            Thread.sleep(50)
        }
    }

    fun sendAndShouldReceiveOpcode(client: Client, expectedOpcode: Int, packet: Packet) {
        sendAndShouldReceiveCallback(client, packet) { opcode: Int, _: Packet, _: Any? ->
            expectedOpcode == opcode
        }
    }

    fun sendAndShouldReceiveCallback(
        client: Client,
        packet: Packet,
        callback: (opcode: Int, packet: Packet, msg: Any?) -> Boolean
    ) {
        val id = client.onIncomingMessage(callback, DEFAULT_TIMEOUT_MS)

        client.send(packet)

        do {
            if (client.isListenerOutdated(id)) {
                throw TimeoutException() // Improve this ?
            }

            Thread.sleep(WAITING_BETWEEN_CHECKS_MS)
        } while (!client.isListenerDone(id))
    }

    companion object {
        const val DEFAULT_TIMEOUT_MS = 5000
        const val WAITING_BETWEEN_CHECKS_MS = 50L
    }

}
