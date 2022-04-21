package sandbox.client

import fr.rob.core.network.v2.AbstractClient
import fr.rob.core.network.v2.session.Session
import fr.rob.core.network.v2.session.SessionSocketInterface
import fr.rob.gateway.message.GatewayProto

class GatewayClient : AbstractClient<GatewayProto.Packet>() {
    override fun onConnectionEstablished(session: Session) {
        this.session = session
    }

    override fun onPacketReceived(packet: GatewayProto.Packet) {
        TODO("Not yet implemented")
    }

    override fun createSession(socket: SessionSocketInterface): Session = Session(socket)
}
