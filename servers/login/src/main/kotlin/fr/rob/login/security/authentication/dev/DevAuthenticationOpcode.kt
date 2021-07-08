package fr.rob.login.security.authentication.dev

import com.google.protobuf.Message
import fr.rob.core.event.EventManagerInterface
import fr.rob.entities.AuthenticationProto
import fr.rob.login.game.SessionInitializerProcess
import fr.rob.login.security.authentication.AuthenticationOpcode
import fr.rob.login.security.authentication.AuthenticationProcess

class DevAuthenticationOpcode(
    authenticationProcess: AuthenticationProcess,
    sessionInitializerProcess: SessionInitializerProcess,
    eventManager: EventManagerInterface
) :
    AuthenticationOpcode(authenticationProcess, sessionInitializerProcess, eventManager) {

    override fun getMessageType(): Message {
        return AuthenticationProto.DevAuthentication.getDefaultInstance()
    }
}
