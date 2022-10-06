package fr.rob.core.opcode.v2.proto

import com.google.protobuf.Message
import fr.rob.core.opcode.v2.OpcodeFunctionInterface

abstract class AbstractMessageOpcodeFunction<FunctionParameters, ProtoMessage> :
    OpcodeFunctionInterface<FunctionParameters> {

    abstract fun createMessageFromPacket(packet: ProtoMessage): Message
    protected abstract fun callForMessage(message: Message, functionParameters: FunctionParameters)
    protected abstract fun getPacketFromParameters(functionParameters: FunctionParameters): ProtoMessage

    override fun call(functionParameters: FunctionParameters) {
        callForMessage(
            createMessageFromPacket(getPacketFromParameters(functionParameters)),
            functionParameters
        )
    }
}
