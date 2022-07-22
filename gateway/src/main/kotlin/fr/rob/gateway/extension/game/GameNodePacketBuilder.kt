package fr.rob.gateway.extension.game

import fr.raven.proto.message.gateway.GatewayProto.Packet
import fr.rob.gateway.network.GatewaySession
import fr.raven.proto.message.game.GameProto.Packet as GamePacket

class GameNodePacketBuilder {
    fun build(gatewayPacket: Packet, session: GatewaySession): GamePacket {
        return GamePacket.newBuilder()
            .setSender(session.accountId ?: 0)
            .setOpcode(gatewayPacket.opcode)
            .setBody(gatewayPacket.body)
            // unsafe input, always set it gateway side
            .setCreatedAt(gatewayPacket.createdAt)
            .build()
    }
}
