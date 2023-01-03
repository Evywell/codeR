package fr.rob.game.infra.command

import com.google.protobuf.Empty
import com.google.protobuf.Message
import fr.raven.proto.message.game.GameProto
import fr.rob.game.domain.entity.ObjectManager
import fr.rob.game.infra.network.session.GatewayGameSession

class RemoveFromWorldOpcode(private val objectManager: ObjectManager) : AuthorizedOpcode() {
    override fun call(message: Message, session: GatewayGameSession, packet: GameProto.Packet) {
        val gameSession = session.findGameSession(packet.sender)
        val player = gameSession.loggedAsPlayer ?: return

        objectManager.removeFromWorld(player)
        session.removeGameSessionFromAccountId(packet.sender)
    }

    override fun createMessageFromPacket(packet: GameProto.Packet): Message = Empty.getDefaultInstance()
}
