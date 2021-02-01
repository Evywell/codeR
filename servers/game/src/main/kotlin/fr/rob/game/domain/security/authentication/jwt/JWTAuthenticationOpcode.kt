package fr.rob.game.domain.security.authentication.jwt

import com.google.protobuf.Message
import fr.rob.entities.AuthenticationProto
import fr.rob.game.domain.process.ProcessManager
import fr.rob.game.domain.security.authentication.AuthenticationOpcode

class JWTAuthenticationOpcode(processManager: ProcessManager) :
    AuthenticationOpcode(processManager) {

    override fun getMessageType(): Message {
        return AuthenticationProto.JWTAuthentication.getDefaultInstance()
    }
}
