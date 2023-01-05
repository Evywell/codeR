package fr.rob.game.infra.command

import fr.rob.game.infra.network.session.exception.GameSessionNotFoundException
import fr.rob.game.infra.opcode.GameNodeFunctionParameters
import fr.rob.game.infra.opcode.GameNodeOpcodeFunction

abstract class AuthenticatedSessionFunction : GameNodeOpcodeFunction() {
    override fun isCallAuthorized(functionParameters: GameNodeFunctionParameters): Boolean {
        return try {
            functionParameters.gatewaySession.findGameSession(getPacketFromParameters(functionParameters).sender)
            true
        } catch (_: GameSessionNotFoundException) {
            false
        }
    }
}
