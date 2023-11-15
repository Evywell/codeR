package fr.rob.game.infra.network.packet

import com.google.protobuf.Message
import fr.raven.proto.message.game.GameProto.Packet
import fr.rob.game.domain.world.function.WorldFunctionRegistry

class BytesToMessageBuilder(private val registry: WorldFunctionRegistry) {
    fun fromPacket(packet: Packet): Message =
        registry.getFunction(packet.opcode).parseFromByteString(packet.body)
}
