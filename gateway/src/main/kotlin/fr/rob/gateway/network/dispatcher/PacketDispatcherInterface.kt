package fr.rob.gateway.network.dispatcher

import fr.rob.gateway.message.GatewayProto.Packet
import fr.rob.gateway.network.GatewaySession

interface PacketDispatcherInterface {
    fun support(packet: Packet): Boolean
    fun dispatch(packet: Packet, session: GatewaySession)
}
