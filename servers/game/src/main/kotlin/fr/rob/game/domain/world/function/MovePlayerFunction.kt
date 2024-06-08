package fr.rob.game.domain.world.function

import com.google.protobuf.ByteString
import com.google.protobuf.Message
import fr.raven.proto.message.game.MovementProto
import fr.raven.proto.message.game.MovementProto.SMovementInfo
import fr.rob.game.domain.entity.Position
import fr.rob.game.domain.entity.movement.Movable
import fr.rob.game.domain.player.session.GameSession

class MovePlayerFunction : WorldFunctionInterface {
    override fun invoke(sender: GameSession, opcode: Int, message: Message) {
        message as SMovementInfo

        sender.controlledWorldObject?.getTrait<Movable>()?.ifPresent { movable ->
            movable.moveToPosition(
                Position(message.position.posX, message.position.posY, message.position.posZ, message.position.orientation),
                Movable.Movement(getDirectionFromMessage(message.direction), getPhaseFromMessage(message.phase)),
            )
        }
    }

    override fun parseFromByteString(data: ByteString): Message = SMovementInfo.parseFrom(data)

    private fun getDirectionFromMessage(message: MovementProto.MovementDirectionType): Movable.DirectionType =
        when (message) {
            MovementProto.MovementDirectionType.TYPE_FORWARD -> Movable.DirectionType.FORWARD
            MovementProto.MovementDirectionType.UNRECOGNIZED -> Movable.DirectionType.NONE
        }

    private fun getPhaseFromMessage(message: MovementProto.MovementPhase): Movable.Phase =
        when (message) {
            MovementProto.MovementPhase.PHASE_BEGIN -> Movable.Phase.MOVING
            MovementProto.MovementPhase.PHASE_END -> Movable.Phase.STOPPED
            MovementProto.MovementPhase.UNRECOGNIZED -> Movable.Phase.STOPPED
        }
}
