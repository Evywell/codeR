package fr.rob.login.security.authentication.dev

import com.google.protobuf.Message
import fr.rob.core.process.ProcessManager
import fr.rob.entities.AuthenticationProto
import fr.rob.login.security.authentication.AuthenticationOpcode

class DevAuthenticationOpcode(processManager: ProcessManager) :
    AuthenticationOpcode(processManager) {

    override fun getMessageType(): Message {
        return AuthenticationProto.JWTAuthentication.getDefaultInstance()
    }
}
