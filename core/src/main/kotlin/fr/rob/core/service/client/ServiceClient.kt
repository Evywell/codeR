package fr.rob.core.service.client

import fr.rob.core.network.Packet
import fr.rob.core.network.session.Session
import fr.rob.core.network.v2.AbstractClient

class ServiceClient : AbstractClient() {
    override fun onConnectionEstablished(session: Session) {
        this.session = session
    }

    override fun onPacketReceived(packet: Packet) {
        TODO("Not yet implemented")
    }

    override fun createSession(): Session {
        TODO("Not yet implemented")
    }
}
