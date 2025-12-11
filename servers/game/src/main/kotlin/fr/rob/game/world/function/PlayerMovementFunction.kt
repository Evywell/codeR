package fr.rob.game.world.function

import com.google.protobuf.ByteString
import com.google.protobuf.Message
import fr.raven.proto.message.game.MovementProto
import fr.rob.game.player.action.MovementCommand
import fr.rob.game.player.action.MovementHandler
import fr.rob.game.entity.Movement
import fr.rob.game.player.session.GameSession

class PlayerMovementFunction : WorldFunctionInterface {
    override fun invoke(sender: GameSession, opcode: Int, message: Message) {
        message as MovementProto.ProceedMovement

        val movementCommand = MovementCommand(
            sender,
            getMovementDirectionType(message),
            message.orientation,
        )

        MovementHandler().execute(movementCommand)
    }

    override fun parseFromByteString(data: ByteString): Message =
        MovementProto.ProceedMovement.parseFrom(data)

    private fun getMovementDirectionType(message: MovementProto.ProceedMovement): Movement.MovementDirectionType {
        return when (message.direction) {
            MovementProto.MovementDirectionType.TYPE_FORWARD -> Movement.MovementDirectionType.FORWARD
            else -> Movement.MovementDirectionType.FORWARD
        }
    }
}
