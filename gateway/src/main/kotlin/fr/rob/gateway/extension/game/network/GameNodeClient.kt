package fr.rob.gateway.extension.game.network

import fr.rob.core.network.v2.AbstractClient
import fr.rob.core.network.v2.session.Session
import fr.rob.core.network.v2.session.SessionSocketInterface
import fr.rob.gateway.message.extension.game.GameProto
import fr.rob.gateway.message.extension.game.GameProto.Packet as GamePacket

class GameNodeClient : AbstractClient<GamePacket>() {
    override fun onConnectionEstablished(session: Session) {
        TODO("Not yet implemented")
    }

    override fun onPacketReceived(packet: GameProto.Packet) {
        TODO("Not yet implemented")
    }

    override fun createSession(socket: SessionSocketInterface): Session {
        TODO("Not yet implemented")
    }
}
