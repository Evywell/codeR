package fr.rob.game.infra.command

import com.google.protobuf.Message
import fr.raven.proto.message.game.GameProto
import fr.rob.game.app.player.action.CreatePlayerIntoWorldCommand
import fr.rob.game.app.player.action.CreatePlayerIntoWorldHandler
import fr.rob.game.command.message.LogIntoWorldProto.LogIntoWorld
import fr.rob.game.domain.character.waitingroom.CharacterWaitingRoom
import fr.rob.game.infra.network.session.GatewayGameSession
import fr.rob.game.infra.opcode.GameOpcodeFunctionInterface

class LogIntoWorldOpcode(
    private val createPlayerIntoWorldHandler: CreatePlayerIntoWorldHandler,
    private val waitingRoom: CharacterWaitingRoom
) : GameOpcodeFunctionInterface {
    override fun call(message: Message, session: GatewayGameSession, packet: GameProto.Packet) {
        message as LogIntoWorld

        val gameSession = session.findGameSession(packet.sender)
        val waitingCharacter = waitingRoom.leave(packet.sender.toString())

        createPlayerIntoWorldHandler.execute(
            CreatePlayerIntoWorldCommand(
                gameSession,
                waitingCharacter.get().characterId,
                waitingCharacter.get().mapInstance
            )
        )
    }

    override fun createMessageFromPacket(packet: GameProto.Packet): Message =
        LogIntoWorld.parseFrom(packet.body)

    override fun isCallAuthorized(session: GatewayGameSession, packet: GameProto.Packet): Boolean =
        waitingRoom.isWaiting(packet.sender.toString())
}
