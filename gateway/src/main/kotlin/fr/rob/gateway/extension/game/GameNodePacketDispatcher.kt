package fr.rob.gateway.extension.game

import fr.raven.proto.message.gateway.GatewayProto.Packet
import fr.rob.gateway.network.GatewaySession
import fr.rob.gateway.network.dispatcher.PacketDispatcherInterface

class GameNodePacketDispatcher(
    private val gameNodePacketBuilder: GameNodePacketBuilder,
) : PacketDispatcherInterface {
    override fun support(packet: Packet, session: GatewaySession): Boolean =
        session.isAuthenticated && Packet.Context.GAME == packet.context

    override fun dispatch(packet: Packet, session: GatewaySession) {
        // retrieve the Game node
        // Send the packet to the socket
        val gameNodePacket = gameNodePacketBuilder.build(packet, session)
        session.currentGameNode?.send(gameNodePacket)
    }
}
