package fr.rob.login.security.authentication.jwt

import com.google.protobuf.Message
import fr.rob.core.process.ProcessManager
import fr.rob.entities.AuthenticationProto
import fr.rob.login.security.authentication.AuthenticationOpcode

class JWTAuthenticationOpcode(processManager: ProcessManager) :
    AuthenticationOpcode(processManager) {

    override fun getMessageType(): Message {
        return AuthenticationProto.JWTAuthentication.getDefaultInstance()
    }
}
