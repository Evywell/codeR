package sandbox.client

import fr.rob.core.network.v2.AbstractClient
import fr.rob.core.network.v2.session.Session
import fr.rob.core.network.v2.session.SessionSocketInterface
import fr.rob.gateway.message.extension.game.GameProto.Packet

class GameClient : AbstractClient<Packet>() {
    override fun onConnectionEstablished(session: Session) {
        this.session = session
    }

    override fun createSession(socket: SessionSocketInterface): Session = Session(socket)

    override fun onPacketReceived(packet: Packet) {
        val time = System.currentTimeMillis() - packet.createdAt

        println("Received in $time ms")
    }
}
