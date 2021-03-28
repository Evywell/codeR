package fr.rob.login.security.authentication

import fr.rob.core.network.session.Session
import fr.rob.core.opcode.ProtobufOpcodeFunction
import fr.rob.core.process.ProcessManager

abstract class AuthenticationOpcode(private val processManager: ProcessManager) :
    ProtobufOpcodeFunction(false) {

    override fun call(session: Session, message: Any) {
        val authenticationProcess = processManager.makeProcess(AuthenticationProcess::class)

        authenticationProcess.authenticate(session, message)
    }
}
