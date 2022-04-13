package fr.rob.core.test.cucumber.service

import fr.rob.core.network.Packet
import fr.rob.core.network.v2.session.SessionSocketInterface

class LocalSessionSocket(private val client: Client) : SessionSocketInterface {

    override fun getIp(): String = "127.0.0.1"
    override fun send(data: Any) {
        client.receiveMessage(data as Packet)
    }

    override fun close() {}
}
