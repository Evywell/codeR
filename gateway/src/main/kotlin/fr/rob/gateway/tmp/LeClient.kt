package fr.rob.gateway.tmp

import fr.rob.core.network.v2.AbstractClient
import fr.rob.core.network.v2.session.Session
import fr.rob.core.network.v2.session.SessionSocketInterface
import fr.rob.gateway.message.GatewayProto.Packet

class LeClient : AbstractClient<Packet>() {
    override fun onConnectionEstablished(session: Session) {
        this.session = session
    }

    override fun onPacketReceived(packet: Packet) {
        TODO("Not yet implemented")
    }

    override fun createSession(socket: SessionSocketInterface): Session = Session(socket)
}
