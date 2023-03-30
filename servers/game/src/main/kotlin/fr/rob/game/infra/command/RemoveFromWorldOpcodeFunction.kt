package fr.rob.game.infra.command

import com.google.protobuf.Empty
import com.google.protobuf.Message
import fr.raven.proto.message.game.GameProto
import fr.rob.game.domain.entity.ObjectManager
import fr.rob.game.infra.opcode.GameNodeFunctionParameters

class RemoveFromWorldOpcodeFunction(private val objectManager: ObjectManager) : AuthenticatedSessionFunction() {
    override fun createMessageFromPacket(packet: GameProto.Packet): Message = Empty.getDefaultInstance()

    override fun callForMessage(message: Message, functionParameters: GameNodeFunctionParameters) {
        val gatewaySession = functionParameters.gatewaySession
        val packet = getPacketFromParameters(functionParameters)
        val gameSession = gatewaySession.findGameSession(packet.sender)
        val player = gameSession.loggedAsPlayer ?: return

        objectManager.removeFromWorld(player)
        gatewaySession.removeGameSessionFromAccountId(packet.sender)
    }
}
