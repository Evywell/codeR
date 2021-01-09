package fr.rob.game.application.opcode

import com.google.protobuf.Message
import fr.rob.game.domain.opcode.OpcodeFunction
import fr.rob.game.domain.network.packet.Packet

abstract class ProtobufOpcodeFunction : OpcodeFunction() {

    override fun createMessageFromPacket(packet: Packet): Any =
        getMessageType().parserForType.parseFrom(packet.toByteArray())

    abstract fun getMessageType(): Message
}