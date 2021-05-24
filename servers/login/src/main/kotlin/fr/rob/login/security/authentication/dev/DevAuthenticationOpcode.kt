package fr.rob.login.security.authentication.dev

import com.google.protobuf.Message
import fr.rob.entities.AuthenticationProto
import fr.rob.login.game.SessionInitializerProcess
import fr.rob.login.security.authentication.AuthenticationOpcode
import fr.rob.login.security.authentication.AuthenticationProcess

class DevAuthenticationOpcode(
    authenticationProcess: AuthenticationProcess,
    sessionInitializerProcess: SessionInitializerProcess
) :
    AuthenticationOpcode(authenticationProcess, sessionInitializerProcess) {

    override fun getMessageType(): Message {
        return AuthenticationProto.DevAuthentication.getDefaultInstance()
    }
}
