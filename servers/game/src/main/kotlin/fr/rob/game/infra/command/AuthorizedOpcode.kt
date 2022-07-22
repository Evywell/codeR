package fr.rob.game.infra.command

import fr.raven.proto.message.game.GameProto
import fr.rob.game.infra.network.session.GatewayGameSession
import fr.rob.game.infra.network.session.exception.GameSessionNotFoundException
import fr.rob.game.infra.opcode.GameOpcodeFunctionInterface

abstract class AuthorizedOpcode : GameOpcodeFunctionInterface {
    override fun isCallAuthorized(session: GatewayGameSession, packet: GameProto.Packet): Boolean {
        return try {
            session.findGameSession(packet.sender)
            true
        } catch (_: GameSessionNotFoundException) {
            false
        }
    }
}
