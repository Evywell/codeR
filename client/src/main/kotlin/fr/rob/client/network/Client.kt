package fr.rob.client.network

import fr.rob.core.network.Packet
import fr.rob.core.network.v2.AbstractClient
import fr.rob.core.network.v2.session.Session
import fr.rob.core.network.v2.session.SessionSocketInterface

class Client : AbstractClient<Packet>() {
    override fun onConnectionEstablished(session: Session) {
        TODO("Not yet implemented")
    }

    override fun onPacketReceived(packet: Packet) {
        TODO("Not yet implemented")
    }

    override fun createSession(socket: SessionSocketInterface): Session = ClientSession(socket)
}
