package fr.rob.game.network

import fr.raven.log.LoggerInterface
import fr.raven.proto.message.game.GameProto.Packet
import fr.rob.core.network.v2.Server
import fr.rob.core.network.v2.session.Session
import fr.rob.core.network.v2.session.SessionSocketInterface
import fr.rob.game.world.packet.WorldPacket
import fr.rob.game.world.packet.WorldPacketQueue
import fr.rob.game.network.packet.BytesToMessageBuilder
import fr.rob.game.network.session.GatewayGameSession
import fr.rob.game.network.opcode.OPCODES_MAP
import com.google.protobuf.InvalidProtocolBufferException

class GameNodeServer(
    private val worldPacketQueue: WorldPacketQueue,
    private val bytesToMessageBuilder: BytesToMessageBuilder,
    private val logger: LoggerInterface,
) : Server<Packet>() {

    override fun onPacketReceived(session: Session, packet: Packet) {
        session as GatewayGameSession
        logger.debug("[GameNodeServer] Packet received with code ${OPCODES_MAP[packet.opcode]} (${packet.opcode})")

        val message = try {
            bytesToMessageBuilder.fromPacket(packet)
        } catch (e: InvalidProtocolBufferException) {
            logger.warning("[GameNodeServer] Malformed packet with opcode {} from account {}", packet.opcode, packet.sender, e)
            return
        }

        if (message == null) {
            logger.warning("[GameNodeServer] Unknown opcode {} from account {}", packet.opcode, packet.sender)
            return
        }

        val gameSession = session.findOrCreateGameSession(packet.sender)

        worldPacketQueue.enqueue(WorldPacket(gameSession, packet.opcode, message))
    }

    override fun onConnectionClosed(session: Session) {
        if (session !is GatewayGameSession) {
            return
        }

        session.getGameSessions().forEach { gameSession ->
            worldPacketQueue.removeQueue(gameSession)
            session.removeGameSessionFromAccountId(gameSession.accountId)
        }
    }

    override fun createSession(socket: SessionSocketInterface): Session =
        GatewayGameSession(logger, socket)
}
