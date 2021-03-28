package fr.rob.core.opcode

import com.google.protobuf.Message
import fr.rob.core.network.Packet

abstract class ProtobufOpcodeFunction(authenticationNeeded: Boolean = true) : OpcodeFunction(authenticationNeeded) {

    override fun createMessageFromPacket(packet: Packet): Any =
        getMessageType().parserForType.parseFrom(packet.toByteArray())

    abstract fun getMessageType(): Message
}
