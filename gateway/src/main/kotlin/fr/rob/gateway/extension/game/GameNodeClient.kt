package fr.rob.gateway.extension.game

import fr.rob.core.network.v2.AbstractClient
import fr.rob.core.network.v2.session.Session
import fr.rob.core.network.v2.session.SessionSocketInterface
import fr.rob.gateway.message.extension.game.GameProto.Packet

class GameNodeClient : AbstractClient<Packet>() {
    override fun onConnectionEstablished(session: Session) {
        this.session = session
    }

    override fun onPacketReceived(packet: Packet) {
        val time = System.currentTimeMillis() - packet.createdAt
        println("$time ms")
    }

    override fun createSession(socket: SessionSocketInterface): Session = Session(socket)
}
