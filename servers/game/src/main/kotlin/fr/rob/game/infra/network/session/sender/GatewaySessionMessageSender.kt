package fr.rob.game.infra.network.session.sender

import com.google.protobuf.Message
import fr.raven.proto.message.game.GameProto
import fr.raven.proto.message.game.NearbyObjectOpcodeProto
import fr.rob.game.app.player.message.NearbyObjectMessage
import fr.rob.game.domain.player.session.GameMessageHolder
import fr.rob.game.domain.player.session.GameSession
import fr.rob.game.domain.player.session.SessionMessageSenderInterface
import fr.rob.game.infra.network.session.GatewayGameSession

class GatewaySessionMessageSender(private val gatewaySession: GatewayGameSession) : SessionMessageSenderInterface {
    override fun send(session: GameSession, message: GameMessageHolder) {
        val sender = session.accountId

        val gamePacket = GameProto.Packet.newBuilder()
            .setSender(sender)
            .setOpcode(message.opcode)
            .setBody(toProtoMessage(message.body).toByteString())
            .build()

        gatewaySession.send(gamePacket)
    }

    private fun fromNearbyObjectMessage(message: NearbyObjectMessage): Message =
        NearbyObjectOpcodeProto.NearbyObjectOpcode.newBuilder()
            .setGuid(message.objectId.getRawValue())
            .setPosX(message.position.x)
            .setPosY(message.position.y)
            .setPosZ(message.position.z)
            .build()

    private fun toProtoMessage(message: Any): Message {
        when (message) {
            is NearbyObjectMessage -> return fromNearbyObjectMessage(message)
        }

        throw RuntimeException("No message builder found for ${message.javaClass.name}")
    }
}
