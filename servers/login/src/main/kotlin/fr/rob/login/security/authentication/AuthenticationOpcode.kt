package fr.rob.login.security.authentication

import fr.rob.core.network.Packet
import fr.rob.core.network.session.Session
import fr.rob.core.opcode.ProtobufOpcodeFunction
import fr.rob.core.process.ProcessManager
import fr.rob.entities.AuthenticationProto
import fr.rob.login.opcode.ServerOpcodeLogin

abstract class AuthenticationOpcode(private val processManager: ProcessManager) :
    ProtobufOpcodeFunction(false) {

    override fun call(session: Session, message: Any) {
        val authenticationProcess = processManager.makeProcess(AuthenticationProcess::class)

        val authenticationResult = AuthenticationProto.AuthenticationResult.newBuilder()

        if (authenticationProcess.authenticate(session, message)) {
            authenticationResult.result = AUTHENTICATION_RESULT_SUCCESS
        } else {
            authenticationResult.result = AUTHENTICATION_RESULT_ERROR
            authenticationResult.code = AUTHENTICATION_CODE_BAD_CREDENTIALS
        }

        session.send(Packet(ServerOpcodeLogin.AUTHENTICATION_RESULT, authenticationResult.build().toByteArray()))
    }

    companion object {
        const val AUTHENTICATION_RESULT_ERROR = 0
        const val AUTHENTICATION_RESULT_SUCCESS = 1

        const val AUTHENTICATION_CODE_BAD_CREDENTIALS = "bad_credentials"
    }
}
