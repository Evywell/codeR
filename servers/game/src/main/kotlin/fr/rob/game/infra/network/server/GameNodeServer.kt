package fr.rob.game.infra.network.server

import fr.raven.log.LoggerInterface
import fr.raven.proto.message.game.GameProto.Packet
import fr.rob.core.network.v2.Server
import fr.rob.core.network.v2.session.Session
import fr.rob.core.network.v2.session.SessionSocketInterface
import fr.rob.game.infra.network.session.GameSessionUpdater
import fr.rob.game.infra.network.session.GatewayGameSession
import fr.rob.game.infra.opcode.GameNodeOpcodeHandler

class GameNodeServer(
    private val gameSessionUpdater: GameSessionUpdater,
    private val opcodeHandler: GameNodeOpcodeHandler,
    private val logger: LoggerInterface,
) : Server<Packet>() {

    override fun onPacketReceived(session: Session, packet: Packet) {
        session as GatewayGameSession
        logger.debug("[GameNodeServer] Packet received with code ${packet.opcode}")
        session.putInQueue(packet)
    }

    override fun createSession(socket: SessionSocketInterface): Session {
        val session = GatewayGameSession(opcodeHandler, socket)
        gameSessionUpdater.addSession(session)

        return session
    }
}
