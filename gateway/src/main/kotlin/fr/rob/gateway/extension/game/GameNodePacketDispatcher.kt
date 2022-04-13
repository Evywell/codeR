package fr.rob.gateway.extension.game

import fr.rob.gateway.message.GatewayProto.Packet
import fr.rob.gateway.network.GatewaySession
import fr.rob.gateway.network.dispatcher.PacketDispatcherInterface

class GameNodePacketDispatcher(
    private val gameNodePacketBuilder: GameNodePacketBuilder,
    private val gameNode: GameNode
) : PacketDispatcherInterface {
    override fun support(packet: Packet): Boolean =
        Packet.Context.GAME == packet.context

    override fun dispatch(packet: Packet, session: GatewaySession) {
        // retrieve the Game node
        // Send the packet to the socket
        gameNode.send(gameNodePacketBuilder.build(packet, session))
    }
}
