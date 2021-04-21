package fr.rob.login.test.feature.service.network

import fr.rob.core.network.Packet
import fr.rob.core.network.session.Session

class TestSession(private val client: Client): Session() {

    override fun getIp(): String = "127.0.0.1"

    override fun send(packet: Packet) {
        client.incomingMessage(packet)
    }

    override fun close() { }
}
