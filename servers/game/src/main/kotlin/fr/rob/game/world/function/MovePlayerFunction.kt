package fr.rob.game.world.function

import com.google.protobuf.ByteString
import com.google.protobuf.Message
import fr.raven.proto.message.game.MovementProto
import fr.raven.proto.message.game.MovementProto.Direction
import fr.raven.proto.message.game.MovementProto.SMovementInfo
import fr.rob.game.behavior.MovableBehavior
import fr.rob.game.component.MovementComponent
import fr.rob.game.entity.Position
import fr.rob.game.map.maths.Vector3f
import fr.rob.game.player.session.GameSession

class MovePlayerFunction : WorldFunctionInterface {
    override fun invoke(sender: GameSession, opcode: Int, message: Message) {
        message as SMovementInfo

        sender.controlledWorldObject?.getBehavior<MovableBehavior>()?.moveObjectToPosition(
            sender.controlledWorldObject!!,
            Position(message.position.posX, message.position.posY, message.position.posZ, message.position.orientation),
            MovementComponent.MovementInfo(getDirectionFromMessage(message.direction), getPhaseFromMessage(message.phase))
        )
    }

    override fun parseFromByteString(data: ByteString): Message = SMovementInfo.parseFrom(data)

    private fun getDirectionFromMessage(direction: Direction): Vector3f = Vector3f(direction.x, direction.y, direction.z)

    private fun getPhaseFromMessage(message: MovementProto.MovementPhase): MovementComponent.Phase =
        when (message) {
            MovementProto.MovementPhase.PHASE_BEGIN -> MovementComponent.Phase.MOVING
            MovementProto.MovementPhase.PHASE_END -> MovementComponent.Phase.STOPPED
            MovementProto.MovementPhase.UNRECOGNIZED -> MovementComponent.Phase.STOPPED
        }
}
