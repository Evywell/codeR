package fr.rob.game.world.function

import com.google.protobuf.ByteString
import com.google.protobuf.Message
import fr.raven.proto.message.game.MovementProto.CheatTeleport
import fr.rob.game.entity.WorldObject
import fr.rob.game.player.session.GameSession

class CheatTeleportFunction : InWorldFunctionInterface {
    override fun invoke(sender: GameSession, controlled: WorldObject, message: Message) {
        message as CheatTeleport

        controlled.setPosition(
            message.position.posX,
            message.position.posY,
            message.position.posZ,
            message.position.orientation,
        )
    }

    override fun parseFromByteString(data: ByteString): Message = CheatTeleport.parseFrom(data)
}
