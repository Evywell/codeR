package fr.rob.game.infra.network.server

import fr.raven.log.LoggerInterface
import fr.raven.proto.message.game.GameProto.Packet
import fr.rob.core.network.v2.Server
import fr.rob.core.network.v2.session.Session
import fr.rob.core.network.v2.session.SessionSocketInterface
import fr.rob.game.domain.world.packet.WorldPacket
import fr.rob.game.domain.world.packet.WorldPacketQueue
import fr.rob.game.infra.network.packet.BytesToMessageBuilder
import fr.rob.game.infra.network.session.GatewayGameSession
import fr.rob.game.infra.network.session.exception.GameSessionNotFoundException
import fr.rob.game.infra.opcode.OPCODES_MAP

class GameNodeServer(
    private val worldPacketQueue: WorldPacketQueue,
    private val bytesToMessageBuilder: BytesToMessageBuilder,
    private val logger: LoggerInterface,
) : Server<Packet>() {

    override fun onPacketReceived(session: Session, packet: Packet) {
        session as GatewayGameSession
        logger.debug("[GameNodeServer] Packet received with code ${OPCODES_MAP[packet.opcode]} (${packet.opcode})")

        val gameSession = try {
            session.findGameSession(packet.sender)
        } catch (_: GameSessionNotFoundException) {
            session.createGameSession(packet.sender)
        }

        worldPacketQueue.enqueue(WorldPacket(gameSession, packet.opcode, bytesToMessageBuilder.fromPacket(packet)))
    }

    override fun createSession(socket: SessionSocketInterface): Session =
        GatewayGameSession(logger, socket)
}
