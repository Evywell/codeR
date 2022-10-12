package fr.rob.game.infra.command

import com.google.protobuf.Message
import fr.raven.proto.message.game.GameProto
import fr.rob.game.domain.character.waitingroom.CharacterWaitingRoom
import fr.rob.game.infra.network.session.GatewayGameSession

class JoinWorldOpcode(private val waitingRoom: CharacterWaitingRoom) : AuthorizedOpcode() {
    override fun call(message: Message, session: GatewayGameSession, packet: GameProto.Packet) {
        TODO("Not yet implemented")
    }

    override fun createMessageFromPacket(packet: GameProto.Packet): Message {
        TODO("Not yet implemented")
    }
}
