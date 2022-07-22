package fr.rob.game.infra.opcode

import com.google.protobuf.Message
import fr.raven.proto.message.game.GameProto.Packet
import fr.rob.game.app.player.action.CreatePlayerIntoWorldHandler
import fr.rob.game.infra.command.InitializeOpcode
import fr.rob.game.infra.command.LogIntoWorldOpcode
import fr.rob.game.infra.network.session.GatewayGameSession

class GameNodeOpcodeHandler(createPlayerIntoWorldHandler: CreatePlayerIntoWorldHandler) {
    private val opcodeFunctions = HashMap<Int, GameOpcodeFunctionInterface>()

    init {
        opcodeFunctions[0x00] = InitializeOpcode()
        opcodeFunctions[0x01] = LogIntoWorldOpcode(createPlayerIntoWorldHandler)
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
        return opcodeFunctions[opcode] ?: throw Exception("No function linked to that opcode")
    }

    data class PacketHolder(val packet: Packet, val message: Message)
}
