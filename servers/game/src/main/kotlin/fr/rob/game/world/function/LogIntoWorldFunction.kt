package fr.rob.game.world.function

import com.google.protobuf.ByteString
import com.google.protobuf.Message
import fr.raven.proto.message.game.setup.LogIntoWorldProto
import fr.rob.game.player.action.CreatePlayerIntoWorldCommand
import fr.rob.game.player.action.CreatePlayerIntoWorldHandler
import fr.rob.game.instance.InstanceManager
import fr.rob.game.player.session.GameSession

class LogIntoWorldFunction(
    private val createPlayerIntoWorldHandler: CreatePlayerIntoWorldHandler,
    private val instanceManager: InstanceManager,
) : WorldFunctionInterface {
    override fun invoke(sender: GameSession, opcode: Int, message: Message) {
        message as LogIntoWorldProto.LogIntoWorld

        createPlayerIntoWorldHandler.execute(
            CreatePlayerIntoWorldCommand(
                sender,
                message.characterId,
                instanceManager.retrieve(1), // @todo change this
            ),
        )
    }

    override fun parseFromByteString(data: ByteString): Message =
        LogIntoWorldProto.LogIntoWorld.parseFrom(data)
}
