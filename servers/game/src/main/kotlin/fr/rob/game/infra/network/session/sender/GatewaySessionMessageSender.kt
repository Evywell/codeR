package fr.rob.game.infra.network.session.sender

import com.google.protobuf.Message
import fr.raven.log.LoggerInterface
import fr.raven.proto.message.game.GameProto
import fr.raven.proto.message.game.MovementProto.MovementHeartbeat
import fr.raven.proto.message.game.NearbyObjectOpcodeProto
import fr.raven.proto.message.game.PlayerProto.PlayerDescription
import fr.raven.proto.message.game.PositionProto
import fr.rob.game.app.player.message.MovementHeartbeatMessage
import fr.rob.game.app.player.message.NearbyObjectMessage
import fr.rob.game.app.player.message.PlayerDescriptionMessage
import fr.rob.game.domain.player.session.GameMessageHolder
import fr.rob.game.domain.player.session.GameSession
import fr.rob.game.domain.player.session.SessionMessageSenderInterface
import fr.rob.game.infra.network.session.GatewayGameSession
import fr.rob.game.infra.opcode.OPCODES_MAP

class GatewaySessionMessageSender(
    private val gatewaySession: GatewayGameSession,
    private val logger: LoggerInterface
) : SessionMessageSenderInterface {
    override fun send(session: GameSession, message: GameMessageHolder) {
        val sender = session.accountId

        val gamePacket = GameProto.Packet.newBuilder()
            .setSender(sender)
            .setOpcode(message.opcode)
            .setBody(toProtoMessage(message.body).toByteString())
            .build()

        logger.debug("Sending message ${OPCODES_MAP[message.opcode]} with size ${gamePacket.toByteArray().size} bytes")

        gatewaySession.send(gamePacket)
    }

    private fun fromPlayerDescriptionMessage(message: PlayerDescriptionMessage): Message =
        PlayerDescription.newBuilder()
            .setGuid(message.guid.getRawValue())
            .setName(message.name)
            .build()

    private fun fromNearbyObjectMessage(message: NearbyObjectMessage): Message =
        NearbyObjectOpcodeProto.NearbyObjectOpcode.newBuilder()
            .setGuid(message.objectId.getRawValue())
            .setPosX(message.position.x)
            .setPosY(message.position.y)
            .setPosZ(message.position.z)
            .build()

    private fun fromMovementHeartbeatMessage(message: MovementHeartbeatMessage): Message =
        MovementHeartbeat.newBuilder()
            .setGuid(message.objectId.getRawValue())
            .setPosition(
                PositionProto.Position.newBuilder()
                    .setPosX(message.position.x)
                    .setPosY(message.position.y)
                    .setPosZ(message.position.z)
                    .setOrientation(message.position.orientation)
                    .build()
            )
            .build()

    private fun toProtoMessage(message: Any): Message {
        when (message) {
            is PlayerDescriptionMessage -> return fromPlayerDescriptionMessage(message)
            is NearbyObjectMessage -> return fromNearbyObjectMessage(message)
            is MovementHeartbeatMessage -> return fromMovementHeartbeatMessage(message)
        }

        throw RuntimeException("No message builder found for ${message.javaClass.name}")
    }
}
