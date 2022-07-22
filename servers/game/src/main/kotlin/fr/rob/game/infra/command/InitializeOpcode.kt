package fr.rob.game.infra.command

import com.google.protobuf.Message
import fr.raven.proto.message.game.GameProto.Packet
import fr.raven.proto.message.game.setup.InitializeOpcodeProto
import fr.rob.game.infra.network.session.GatewayGameSession
import fr.rob.game.infra.opcode.GameOpcodeFunctionInterface

class InitializeOpcode : GameOpcodeFunctionInterface {
    override fun call(message: Message, session: GatewayGameSession, packet: Packet) {
        message as InitializeOpcodeProto.Initialize

        val responsePacket = Packet.newBuilder()
            .setOpcode(0x99)
            .setSender(packet.sender)
            .setBody(
                InitializeOpcodeProto.InitializationSucceed.newBuilder()
                    .setActionToInitiate(message.actionToInitiate)
                    .build()
                    .toByteString()
            )
            .build()

        return session.send(responsePacket)
    }

    override fun createMessageFromPacket(packet: Packet): Message = InitializeOpcodeProto.Initialize.parseFrom(packet.body)

    override fun isCallAuthorized(session: GatewayGameSession, packet: Packet): Boolean = true
}
