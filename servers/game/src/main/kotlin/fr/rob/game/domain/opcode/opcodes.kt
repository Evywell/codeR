package fr.rob.game.domain.opcode

import com.google.protobuf.Message
import fr.rob.game.domain.network.packet.Packet

abstract class ProtobufOpcodeFunction(authenticationNeeded: Boolean = true) : OpcodeFunction(authenticationNeeded) {

    override fun createMessageFromPacket(packet: Packet): Any =
        getMessageType().parserForType.parseFrom(packet.toByteArray())

    abstract fun getMessageType(): Message
}
