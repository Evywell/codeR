package fr.rob.game.infra.opcode

import com.google.protobuf.Message
import fr.raven.proto.message.game.GameProto.Packet
import fr.rob.game.app.player.action.CreatePlayerIntoWorldHandler
import fr.rob.game.domain.character.waitingroom.CharacterWaitingRoom
import fr.rob.game.domain.entity.ObjectManager
import fr.rob.game.infra.command.LogIntoWorldOpcode
import fr.rob.game.infra.command.RemoveFromWorldOpcode
import fr.rob.game.infra.network.session.GatewayGameSession
import fr.rob.game.infra.opcode.exception.NoOpcodeFoundException

class GameNodeOpcodeHandler(
    createPlayerIntoWorldHandler: CreatePlayerIntoWorldHandler,
    characterWaitingRoom: CharacterWaitingRoom,
    objectManager: ObjectManager
) {
    private val opcodeFunctions = HashMap<Int, GameOpcodeFunctionInterface>()

    init {
        opcodeFunctions[CMSG_LOG_INTO_WORLD] = LogIntoWorldOpcode(createPlayerIntoWorldHandler, characterWaitingRoom)
        opcodeFunctions[CMSG_REMOVE_FROM_WORLD] = RemoveFromWorldOpcode(objectManager)
    }

    fun process(opcode: Int, packet: Packet, session: GatewayGameSession) {
        val function = opcodeFunctions[opcode]

        if (function != null && function.isCallAuthorized(session, packet)) {
            val message = function.createMessageFromPacket(packet)
            function.call(message, session, packet)
        }
    }

    fun processFromMessage(packet: Packet, session: GatewayGameSession, message: Message) {
        opcodeFunctions[packet.opcode]?.call(message, session, packet)
    }

    fun getFunction(opcode: Int): GameOpcodeFunctionInterface {
        return opcodeFunctions[opcode] ?: throw NoOpcodeFoundException(opcode)
    }

    data class PacketHolder(val packet: Packet, val message: Message)
}
