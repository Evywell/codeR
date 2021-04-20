package fr.rob.login.test.feature.service.network

import fr.rob.core.network.Packet
import fr.rob.entities.AuthenticationProto
import fr.rob.login.opcode.ClientOpcodeLogin
import fr.rob.login.test.feature.ClientApplication
import fr.rob.login.test.feature.Scenario.Companion.DEFAULT_TIMEOUT_MS

class Client(app: ClientApplication) {

    private var server: TestLoginServer? = null
    private val incomingMessageListeners = HashMap<Int, Listener>()
    private val queue = ClientQueue(this, incomingMessageListeners)

    init {
        queue.start()
    }

    fun connectTo(serverToConnect: TestLoginServer) {
        server = serverToConnect

        server!!.incomingConnection(this)
    }

    fun send(packet: Packet) {
        server?.incomingMessage(this, packet)
    }

    fun processMessage(opcode: Int, packet: Packet): Any? {
        var result: Any? = null

        when(opcode) {
            ClientOpcodeLogin.AUTHENTICATE_SESSION -> {
                result = AuthenticationProto.AuthenticationResult.parseFrom(packet.toByteArray())
            }
        }

        return result
    }

    fun isListenerDone(id: Int): Boolean =
        incomingMessageListeners.containsKey(id) && incomingMessageListeners[id]?.isDone ?: false

    fun incomingMessage(packet: Packet) {
        val opcode = packet.readOpcode()

        queue.add(ClientQueueItem(opcode, packet))
    }

    fun onIncomingMessage(callback: (opcode: Int, packet: Packet, msg: Any?) -> Boolean, timeout: Int): Int {
        val listener = Listener(callback, timeout)
        val id = listener.hashCode()

        incomingMessageListeners[id] = listener

        return id
    }

    fun isListenerOutdated(id: Int): Boolean =
        !incomingMessageListeners.containsKey(id) || incomingMessageListeners[id]?.isOutdated ?: false

    fun reset() {
        queue.shutdown()
        server = null
    }
}
