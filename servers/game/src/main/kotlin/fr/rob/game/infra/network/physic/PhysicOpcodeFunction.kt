package fr.rob.game.infra.network.physic

import fr.raven.proto.message.physicbridge.PhysicProto.Packet
import fr.rob.core.opcode.v2.proto.AbstractMessageOpcodeFunction

abstract class PhysicOpcodeFunction : AbstractMessageOpcodeFunction<PhysicFunctionParameters, Packet>() {
    override fun getPacketFromParameters(functionParameters: PhysicFunctionParameters): Packet = functionParameters.packet
}
