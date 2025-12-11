package fr.rob.game.world.function

import com.google.protobuf.ByteString
import com.google.protobuf.Empty
import com.google.protobuf.Message
import fr.rob.game.player.session.GameSession

class RemoveFromWorldFunction : WorldFunctionInterface {
    override fun invoke(sender: GameSession, opcode: Int, message: Message) {
        val player = sender.loggedAsPlayer ?: return

        player.scheduleRemoveFromInstance()
    }

    override fun parseFromByteString(data: ByteString): Message = Empty.getDefaultInstance()
}
