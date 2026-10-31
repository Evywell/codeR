package fr.rob.game.network.packet

import com.google.protobuf.Message
import fr.raven.proto.message.game.GameProto.Packet
import fr.rob.game.world.function.WorldFunctionRegistry

class BytesToMessageBuilder(private val registry: WorldFunctionRegistry) {
    /**
     * Returns null when the opcode is unknown.
     *
     * @throws com.google.protobuf.InvalidProtocolBufferException when the body cannot be parsed
     */
    fun fromPacket(packet: Packet): Message? =
        registry.findFunction(packet.opcode)?.parseFromByteString(packet.body)
}
