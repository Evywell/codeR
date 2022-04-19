package fr.rob.gateway.extension.game

import fr.rob.gateway.message.GatewayProto.Packet
import fr.rob.gateway.network.GatewaySession
import fr.rob.gateway.message.extension.game.GameProto.Packet as GamePacket

class GameNodePacketBuilder {
    fun build(gatewayPacket: Packet, session: GatewaySession): GamePacket {
        return GamePacket.newBuilder()
            .setSender(session.userId.toString())
            .setBody(gatewayPacket.body)
            // unsafe input, always set it gateway side
            .setCreatedAt(gatewayPacket.createdAt)
            .build()
    }
}
