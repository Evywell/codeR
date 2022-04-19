package fr.rob.game.network

import fr.raven.log.LoggerInterface
import fr.rob.core.network.v2.Server
import fr.rob.core.network.v2.session.Session
import fr.rob.core.network.v2.session.SessionSocketInterface
import fr.rob.game.network.session.GameSession
import fr.rob.game.opcode.GameNodeOpcodeHandler
import fr.rob.gateway.message.extension.game.GameProto.Packet

class GameNodeServer(private val logger: LoggerInterface) : Server<Packet>() {

    override fun onPacketReceived(session: Session, packet: Packet) {
        val message = Packet.newBuilder()
            .setCreatedAt(packet.createdAt)
            .build()

        session.send(message)
    }

    override fun createSession(socket: SessionSocketInterface): Session =
        GameSession(GameNodeOpcodeHandler(logger), socket)
}
