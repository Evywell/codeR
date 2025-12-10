package fr.rob.gateway.extension.game

import fr.raven.log.LoggerInterface
import fr.raven.proto.message.game.GameProto.Packet
import fr.raven.proto.message.gateway.GatewayProto
import fr.rob.core.network.v2.AbstractClient
import fr.rob.core.network.v2.session.Session
import fr.rob.core.network.v2.session.SessionSocketInterface
import fr.rob.core.opcode.v2.OpcodeHandler
import fr.rob.core.opcode.v2.exception.OpcodeFunctionNotFoundException
import fr.rob.gateway.extension.game.opcode.GameNodeFunctionParameters
import fr.rob.gateway.extension.game.opcode.GameNodeOpcodeRegistry
import fr.rob.gateway.network.Gateway
import fr.rob.gateway.network.GatewaySession

class GameNodeClient(
    private val nodeLabel: String,
    private val gateway: Gateway,
    private val logger: LoggerInterface
) : AbstractClient<Packet>() {

    private val opcodeHandler: OpcodeHandler<GameNodeFunctionParameters> = OpcodeHandler(
        GameNodeOpcodeRegistry()
    )

    override fun onConnectionEstablished(session: Session) {
        this.session = session
    }

    override fun onConnectionClosed() {
        logger.error("Connection lost with game node $nodeLabel, closing client connections...")

        gateway.gameNodes.findByLabel(nodeLabel)?.let { gameNode -> gateway.closeGameNodeConnection(gameNode) }
    }

    override fun onPacketReceived(packet: Packet) {
        logger.debug("Packet received with opcode ${packet.opcode}")

        val accountId = packet.sender
        val gatewaySession = gateway.findSessionByAccountId(accountId)

        try {
            opcodeHandler.process(
                packet.opcode,
                GameNodeFunctionParameters(packet.opcode, packet, session, gatewaySession)
            )
        } catch (_: OpcodeFunctionNotFoundException) {
            forwardPacketToClient(gatewaySession, packet)
        }
    }

    private fun forwardPacketToClient(gatewaySession: GatewaySession, packet: Packet) {
        gatewaySession.send(
            GatewayProto.Packet.newBuilder()
                .setContext(GatewayProto.Packet.Context.GAME)
                .setOpcode(packet.opcode)
                .setBody(packet.body)
                .build()
        )
    }

    override fun createSession(socket: SessionSocketInterface): Session = Session(socket)
}
