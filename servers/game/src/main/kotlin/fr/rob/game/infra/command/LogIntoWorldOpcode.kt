package fr.rob.game.infra.command

import com.google.protobuf.Message
import fr.raven.proto.message.game.GameProto
import fr.rob.game.app.player.action.CreatePlayerIntoWorldCommand
import fr.rob.game.app.player.action.CreatePlayerIntoWorldHandler
import fr.rob.game.command.message.LogIntoWorldProto.LogIntoWorld
import fr.rob.game.infra.network.session.GatewayGameSession

class LogIntoWorldOpcode(
    private val createPlayerIntoWorldHandler: CreatePlayerIntoWorldHandler,
) : AuthorizedOpcode() {
    override fun call(message: Message, session: GatewayGameSession, packet: GameProto.Packet) {
        message as LogIntoWorld

        createPlayerIntoWorldHandler.execute(
            CreatePlayerIntoWorldCommand(session.findGameSession(packet.sender), message.characterId)
        )
    }

    override fun createMessageFromPacket(packet: GameProto.Packet): Message =
        LogIntoWorld.parseFrom(packet.body)
}
