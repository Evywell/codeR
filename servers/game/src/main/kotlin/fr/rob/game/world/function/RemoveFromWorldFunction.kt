package fr.rob.game.world.function

import com.google.protobuf.ByteString
import com.google.protobuf.Empty
import com.google.protobuf.Message
import fr.rob.game.entity.WorldObject
import fr.rob.game.player.session.GameSession

class RemoveFromWorldFunction : InWorldFunctionInterface {
    override fun invoke(sender: GameSession, controlled: WorldObject, message: Message) {
        controlled.scheduleRemoveFromInstance()
        sender.leaveWorld()
    }

    override fun parseFromByteString(data: ByteString): Message = Empty.getDefaultInstance()
}
