package fr.rob.gateway.extension.game

import fr.raven.log.LoggerInterface
import fr.raven.proto.message.gateway.GatewayProto.Packet
import fr.rob.gateway.network.GatewaySession
import fr.rob.gateway.network.dispatcher.PacketDispatcherInterface

class GameNodePacketDispatcher(
    private val gameNodePacketBuilder: GameNodePacketBuilder,
    private val logger: LoggerInterface,
) : PacketDispatcherInterface {
    override fun support(packet: Packet, session: GatewaySession): Boolean =
        session.isAuthenticated && Packet.Context.GAME == packet.context

    override fun dispatch(packet: Packet, session: GatewaySession) {
        // retrieve the Game node
        // Send the packet to the socket
        val gameNodePacket = gameNodePacketBuilder.build(packet, session)

        if (session.currentGameNode == null) {
            logger.warning("Trying to send a packet to an unknown game node")
        }

        session.currentGameNode?.send(gameNodePacket)
    }
}
