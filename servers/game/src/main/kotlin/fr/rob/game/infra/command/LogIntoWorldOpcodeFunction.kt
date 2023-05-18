package fr.rob.game.infra.command

import com.google.protobuf.Message
import fr.raven.proto.message.game.GameProto
import fr.raven.proto.message.game.setup.LogIntoWorldProto
import fr.rob.game.app.player.action.CreatePlayerIntoWorldCommand
import fr.rob.game.app.player.action.CreatePlayerIntoWorldHandler
import fr.rob.game.domain.character.waitingroom.CharacterWaitingRoom
import fr.rob.game.infra.opcode.GameNodeFunctionParameters
import fr.rob.game.infra.opcode.GameNodeOpcodeFunction

class LogIntoWorldOpcodeFunction(
    private val createPlayerIntoWorldHandler: CreatePlayerIntoWorldHandler,
    private val waitingRoom: CharacterWaitingRoom
) : GameNodeOpcodeFunction() {
    override fun createMessageFromPacket(packet: GameProto.Packet): Message =
        LogIntoWorldProto.LogIntoWorld.parseFrom(packet.body)

    override fun callForMessage(message: Message, functionParameters: GameNodeFunctionParameters) {
        message as LogIntoWorldProto.LogIntoWorld

        val packet = getPacketFromParameters(functionParameters)
        val gameSession = functionParameters.gatewaySession.findGameSession(packet.sender)
        val waitingCharacter = waitingRoom.leave(packet.sender.toString())

        createPlayerIntoWorldHandler.execute(
            CreatePlayerIntoWorldCommand(
                gameSession,
                waitingCharacter.get().characterId,
                waitingCharacter.get().mapInstance
            )
        )
    }

    override fun isCallAuthorized(functionParameters: GameNodeFunctionParameters): Boolean =
        waitingRoom.isWaiting(getPacketFromParameters(functionParameters).sender.toString())
}
