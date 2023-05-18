package fr.rob.game.infra.command

import com.google.protobuf.Message
import fr.raven.proto.message.game.GameProto
import fr.raven.proto.message.game.MovementProto
import fr.rob.game.app.player.action.MovementCommand
import fr.rob.game.app.player.action.MovementHandler
import fr.rob.game.domain.entity.Movement
import fr.rob.game.infra.opcode.GameNodeFunctionParameters

class PlayerMovementOpcodeFunction : AuthenticatedSessionFunction() {
    override fun createMessageFromPacket(packet: GameProto.Packet): Message =
        MovementProto.ProceedMovement.parseFrom(packet.body)

    override fun callForMessage(message: Message, functionParameters: GameNodeFunctionParameters) {
        message as MovementProto.ProceedMovement

        val packet = getPacketFromParameters(functionParameters)
        val gameSession = functionParameters.gatewaySession.findGameSession(packet.sender)

        val movementCommand = MovementCommand(
            gameSession,
            getMovementDirectionType(message),
            message.orientation
        )

        MovementHandler().execute(movementCommand)
    }

    private fun getMovementDirectionType(message: MovementProto.ProceedMovement): Movement.MovementDirectionType {
        return when (message.direction) {
            MovementProto.MovementDirectionType.TYPE_FORWARD -> Movement.MovementDirectionType.FORWARD
            else -> Movement.MovementDirectionType.FORWARD
        }
    }
}
