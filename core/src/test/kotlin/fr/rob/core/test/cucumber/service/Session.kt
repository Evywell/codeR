package fr.rob.core.test.cucumber.service

import fr.rob.core.network.Packet
import fr.rob.core.network.session.Session as AbstractSession

open class Session(protected val client: Client) : AbstractSession() {

    override fun getIp(): String = "127.0.0.1"

    override fun send(packet: Packet) {
        client.receiveMessage(packet)
    }

    override fun close() {}
}
