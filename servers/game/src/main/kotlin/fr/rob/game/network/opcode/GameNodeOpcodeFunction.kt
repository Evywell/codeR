package fr.rob.game.network.opcode

import fr.raven.proto.message.game.GameProto
import fr.rob.core.opcode.v2.proto.AbstractMessageOpcodeFunction

abstract class GameNodeOpcodeFunction : AbstractMessageOpcodeFunction<GameNodeFunctionParameters, GameProto.Packet>() {
    override fun getPacketFromParameters(functionParameters: GameNodeFunctionParameters): GameProto.Packet =
        functionParameters.packet
}
