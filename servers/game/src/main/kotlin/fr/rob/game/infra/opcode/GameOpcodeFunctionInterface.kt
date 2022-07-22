package fr.rob.game.infra.opcode

import com.google.protobuf.Message
import fr.raven.proto.message.game.GameProto.Packet
import fr.rob.game.infra.network.session.GatewayGameSession

interface GameOpcodeFunctionInterface {
    fun call(message: Message, session: GatewayGameSession, packet: Packet)
    fun createMessageFromPacket(packet: Packet): Message
    fun isCallAuthorized(session: GatewayGameSession, packet: Packet): Boolean
}
