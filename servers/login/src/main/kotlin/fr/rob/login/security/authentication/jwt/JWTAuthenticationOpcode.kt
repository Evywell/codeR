package fr.rob.login.security.authentication.jwt

import com.google.protobuf.Message
import fr.rob.core.event.EventManagerInterface
import fr.rob.entities.AuthenticationProto
import fr.rob.login.game.SessionInitializerProcess
import fr.rob.login.security.authentication.AuthenticationOpcode
import fr.rob.login.security.authentication.AuthenticationProcess

class JWTAuthenticationOpcode(
    authenticationProcess: AuthenticationProcess,
    sessionInitializerProcess: SessionInitializerProcess,
    eventManager: EventManagerInterface
) :
    AuthenticationOpcode(authenticationProcess, sessionInitializerProcess, eventManager) {

    override fun getMessageType(): Message {
        return AuthenticationProto.JWTAuthentication.getDefaultInstance()
    }
}
