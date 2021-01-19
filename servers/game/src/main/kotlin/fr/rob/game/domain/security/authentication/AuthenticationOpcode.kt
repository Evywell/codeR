package fr.rob.game.domain.security.authentication

import fr.rob.game.domain.network.session.Session
import fr.rob.game.domain.opcode.ProtobufOpcodeFunction
import fr.rob.game.domain.process.ProcessManager

abstract class AuthenticationOpcode(val processManager: ProcessManager) :
    ProtobufOpcodeFunction(false) {

    override fun call(session: Session, message: Any) {
        val authenticationProcess = processManager.makeProcess(AuthenticationProcess::class)

        authenticationProcess.authenticate(session, message)
    }
}
