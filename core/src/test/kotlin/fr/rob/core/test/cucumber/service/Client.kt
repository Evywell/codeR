package fr.rob.core.test.cucumber.service

import fr.rob.core.network.Packet
import fr.rob.core.network.session.Session
import fr.rob.core.test.cucumber.service.network.MessageReceiverInterface
import fr.rob.core.test.cucumber.service.stack.ClientStack
import fr.rob.core.test.cucumber.service.stack.StackItem

abstract class Client {

    lateinit var server: Server
    lateinit var session: Session

    val isReady
        get() = stack.isReady

    val messages = ArrayList<Message>()

    private val stack by lazy { ClientStack(this, createMessageReceiver()) }

    init {
        stack.start()
    }

    fun connectToServer(serverToConnect: Server) {
        session = createSession()

        server = serverToConnect
        server.registerSession(session.hashCode().toString(), session)
    }

    fun send(packet: Packet) {
        server.receiveMessage(session, packet)
    }

    fun receiveMessage(packet: Packet) {
        stack.pushItem(StackItem(packet.readOpcode(), packet = packet))
    }

    fun stop() {
        messages.clear()
        stack.shutdown()
    }

    protected abstract fun createMessageReceiver(): MessageReceiverInterface

    abstract fun createSession(): Session
}
