package fr.rob.game.world.function

import com.google.protobuf.ByteString
import com.google.protobuf.Message
import fr.rob.game.player.session.GameSession

interface WorldFunctionInterface {
    fun invoke(sender: GameSession, opcode: Int, message: Message)
    fun parseFromByteString(data: ByteString): Message
}
