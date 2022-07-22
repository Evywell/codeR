package fr.rob.gateway.network.dispatcher

import fr.raven.proto.message.gateway.GatewayProto.Packet
import fr.rob.gateway.network.GatewaySession

interface PacketDispatcherInterface {
    fun support(packet: Packet, session: GatewaySession): Boolean
    fun dispatch(packet: Packet, session: GatewaySession)
}
