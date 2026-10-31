package fr.rob.game.world.function

import com.google.protobuf.ByteString
import com.google.protobuf.Message
import fr.raven.proto.message.game.MovementProto.CheatTeleport
import fr.rob.game.component.MovementComponent
import fr.rob.game.entity.Position
import fr.rob.game.entity.WorldObject
import fr.rob.game.entity.movement.MovementValidator
import fr.rob.game.player.message.MovementHeartbeatMessage
import fr.rob.game.player.session.GameSession

class CheatTeleportFunction(private val movementValidator: MovementValidator) : InWorldFunctionInterface {
    override fun invoke(sender: GameSession, controlled: WorldObject, message: Message) {
        message as CheatTeleport

        val destination = Position(message.position.posX, message.position.posY, message.position.posZ, message.position.orientation)

        if (!movementValidator.isValidDestination(controlled, destination)) {
            sender.send(MovementHeartbeatMessage(controlled.guid, controlled.position, controlled.getComponent<MovementComponent>()?.movementInfo))
            return
        }

        controlled.setPosition(destination.x, destination.y, destination.z, destination.orientation)
    }

    override fun parseFromByteString(data: ByteString): Message = CheatTeleport.parseFrom(data)
}
