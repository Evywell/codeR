package fr.rob.login.security.authentication.jwt

import com.google.protobuf.Message
import fr.rob.entities.AuthenticationProto
import fr.rob.login.security.authentication.AuthenticationOpcode
import fr.rob.login.security.authentication.AuthenticationProcess

class JWTAuthenticationOpcode(authenticationProcess: AuthenticationProcess) :
    AuthenticationOpcode(authenticationProcess) {

    override fun getMessageType(): Message {
        return AuthenticationProto.JWTAuthentication.getDefaultInstance()
    }
}
