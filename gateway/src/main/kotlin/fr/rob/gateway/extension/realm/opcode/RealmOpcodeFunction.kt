package fr.rob.gateway.extension.realm.opcode

import fr.rob.core.network.Packet
import fr.rob.core.opcode.v2.proto.AbstractMessageOpcodeFunction

abstract class RealmOpcodeFunction : AbstractMessageOpcodeFunction<RealmFunctionParameters, Packet>() {
    override fun getPacketFromParameters(functionParameters: RealmFunctionParameters): Packet =
        functionParameters.packet
}
