package fr.rob.game.domain.security.authentication

import com.google.protobuf.Message
import fr.rob.game.ENV_DEV
import fr.rob.game.domain.network.session.Session
import fr.rob.game.domain.opcode.ProtobufOpcodeFunction
import fr.rob.game.domain.process.ProcessManager
import fr.rob.game.domain.security.authentication.dev.DevAuthenticationProcess
import fr.rob.game.domain.security.authentication.jwt.JWTAuthenticationProcess
import fr.rob.game.entity.authentication.AuthenticationProto

class AuthenticationOpcode(private val environment: String, private val processManager: ProcessManager) :
    ProtobufOpcodeFunction(false) {

    override fun getMessageType(): Message {
        if (environment === ENV_DEV) {
            return AuthenticationProto.DevAuthentication.getDefaultInstance()
        }

        return AuthenticationProto.JWTAuthentication.getDefaultInstance()
    }

    override fun call(session: Session, message: Any) {
        val authProcess = processManager.makeProcess(AuthenticationProcess::class)

        if (environment === ENV_DEV) {
            val userId = (message as AuthenticationProto.DevAuthentication).userId
            (authProcess as DevAuthenticationProcess).userId = userId
        } else {
            val token = (message as AuthenticationProto.JWTAuthentication).token
            (authProcess as JWTAuthenticationProcess).token = token
        }

        authProcess.authenticate(session)
    }
}
