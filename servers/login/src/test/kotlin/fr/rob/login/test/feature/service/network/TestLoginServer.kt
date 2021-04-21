package fr.rob.login.test.feature.service.network

import fr.rob.core.network.Packet
import fr.rob.core.network.Server
import fr.rob.login.opcode.LoginOpcodeHandler
import fr.rob.login.test.feature.LoginApplication

class TestLoginServer(app: LoginApplication): Server() {

    private val opcodeHandler = LoginOpcodeHandler(app.env, app.processManager, app.logger)
    private val queue = ServerQueue(opcodeHandler)

    var isShutdownSuccessfully = false
        private set
        get() = queue.isShutdownSuccessfully

    init {
        opcodeHandler.initialize()
    }

    override fun start() {
        queue.start()
    }

    fun incomingConnection(client: Client) {
        registerSession(client.hashCode(), TestSession(client))
    }

    fun incomingMessage(client: Client, packet: Packet) {
        val session = sessionFromIdentifier(client.hashCode())

        queue.add(ServerQueueItem(packet.readOpcode(), session, packet))
    }

    fun stop() {
        queue.shutdown()
    }
}
