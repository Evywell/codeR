package fr.rob.gateway.extension.game

import fr.raven.proto.message.gateway.GatewayProto.Packet
import fr.rob.gateway.network.GatewaySession
import fr.raven.proto.message.game.GameProto.Packet as GamePacket

class GameNodePacketBuilder {
    fun build(gatewayPacket: Packet, session: GatewaySession): GamePacket {
        if (session.accountId == null) {
            throw RuntimeException("Trying to send a game packet for not authenticated session")
        }

        return GamePacket.newBuilder()
            .setSender(session.accountId!!)
            .setOpcode(gatewayPacket.opcode)
            .setBody(gatewayPacket.body)
            // unsafe input, always set it gateway side
            .setCreatedAt(gatewayPacket.createdAt)
            .build()
    }
}
