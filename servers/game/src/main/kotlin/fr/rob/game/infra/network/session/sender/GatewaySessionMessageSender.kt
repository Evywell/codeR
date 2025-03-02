package fr.rob.game.infra.network.session.sender

import com.google.protobuf.Message
import fr.raven.log.LoggerInterface
import fr.raven.proto.message.game.DebugProto.DebugSignal
import fr.raven.proto.message.game.GameProto
import fr.raven.proto.message.game.MovementProto
import fr.raven.proto.message.game.MovementProto.Direction
import fr.raven.proto.message.game.MovementProto.MovementHeartbeat
import fr.raven.proto.message.game.NearbyObjectOpcodeProto
import fr.raven.proto.message.game.ObjectSheetProto.ObjectSheetUpdate
import fr.raven.proto.message.game.PlayerProto.PlayerDescription
import fr.raven.proto.message.game.PositionProto
import fr.rob.game.app.player.message.DebugSignalMessage
import fr.rob.game.app.player.message.HealthMessage
import fr.rob.game.app.player.message.MovementHeartbeatMessage
import fr.rob.game.app.player.message.NearbyObjectMessage
import fr.rob.game.app.player.message.ObjectMovingToDestinationMessage
import fr.rob.game.app.player.message.PlayerDescriptionMessage
import fr.rob.game.domain.player.session.GameMessageHolder
import fr.rob.game.domain.player.session.GameSession
import fr.rob.game.domain.player.session.SessionMessageSenderInterface
import fr.rob.game.infra.network.session.GatewayGameSession
import fr.rob.game.infra.opcode.OPCODES_MAP
import fr.rob.game.infra.opcode.SMSG_MOVEMENT_HEARTBEAT

class GatewaySessionMessageSender(
    private val gatewaySession: GatewayGameSession,
    private val logger: LoggerInterface,
) : SessionMessageSenderInterface {
    override fun send(session: GameSession, message: GameMessageHolder) {
        val sender = session.accountId

        val gamePacket = GameProto.Packet.newBuilder()
            .setSender(sender)
            .setOpcode(message.opcode)
            .setBody(toProtoMessage(message.body).toByteString())
            .build()

        if (shouldLogMessageSending(message.opcode)) {
            logger.debug("Sending message ${OPCODES_MAP[message.opcode]} with size ${gamePacket.toByteArray().size} bytes")
        }

        gatewaySession.send(gamePacket)
    }

    private fun shouldLogMessageSending(opcode: Int): Boolean {
        return !opcodesToIgnore.contains(opcode)
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
            .setOrientation(message.position.orientation)
            .build()

    private fun fromMovementHeartbeatMessage(message: MovementHeartbeatMessage): Message {
        val direction = if (message.movement != null) Direction.newBuilder()
            .setX(message.movement.direction.x)
            .setY(message.movement.direction.y)
            .setZ(message.movement.direction.z)
            .build()
            else Direction.getDefaultInstance()

        return MovementHeartbeat.newBuilder()
            .setGuid(message.objectId.getRawValue())
            .setPosition(
                PositionProto.Position.newBuilder()
                    .setPosX(message.position.x)
                    .setPosY(message.position.y)
                    .setPosZ(message.position.z)
                    .setOrientation(message.position.orientation)
                    .build(),
            )
            .setDirection(direction)
            .setPhase(if (message.movement === null || message.movement.isMoving()) MovementProto.MovementPhase.PHASE_BEGIN else MovementProto.MovementPhase.PHASE_END)
            .build()
    }

    private fun fromHealthMessage(message: HealthMessage): Message =
        ObjectSheetUpdate.newBuilder()
            .setGuid(message.objectId.getRawValue())
            .setHealth(message.health)
            .build()

    private fun fromDebugSignalMessage(message: DebugSignalMessage): Message =
        DebugSignal.newBuilder()
            .setName(message.signalName)
            .setValue(message.signalValue)
            .build()

    private fun fromTravelPlanDefinedMessage(message: ObjectMovingToDestinationMessage): Message =
        MovementProto.ObjectMovingToDestination.newBuilder()
            .setGuid(message.objectId.getRawValue())
            .setDestination(
                PositionProto.PositionVec3.newBuilder()
                    .setPosX(message.destination.x)
                    .setPosY(message.destination.y)
                    .setPosZ(message.destination.z)
            )
            .build()

    private fun toProtoMessage(message: Any): Message {
        when (message) {
            is PlayerDescriptionMessage -> return fromPlayerDescriptionMessage(message)
            is NearbyObjectMessage -> return fromNearbyObjectMessage(message)
            is MovementHeartbeatMessage -> return fromMovementHeartbeatMessage(message)
            is HealthMessage -> return fromHealthMessage(message)
            is DebugSignalMessage -> return fromDebugSignalMessage(message)
            is ObjectMovingToDestinationMessage -> return fromTravelPlanDefinedMessage(message)
        }

        throw RuntimeException("No message builder found for ${message.javaClass.name}")
    }

    companion object {
        private val opcodesToIgnore = arrayOf(SMSG_MOVEMENT_HEARTBEAT)
    }
}
