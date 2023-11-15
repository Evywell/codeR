package fr.rob.game.domain.world.function

import com.google.protobuf.ByteString
import com.google.protobuf.Message
import fr.raven.proto.message.game.MovementProto.CheatTeleport
import fr.rob.game.domain.player.session.GameSession

class CheatTeleportFunction : WorldFunctionInterface {
    override fun invoke(sender: GameSession, opcode: Int, message: Message) {
        message as CheatTeleport

        sender.controlledWorldObject?.setPosition(
            message.position.posX,
            message.position.posY,
            message.position.posZ,
            message.position.orientation,
        )
    }

    override fun parseFromByteString(data: ByteString): Message = CheatTeleport.parseFrom(data)
}
