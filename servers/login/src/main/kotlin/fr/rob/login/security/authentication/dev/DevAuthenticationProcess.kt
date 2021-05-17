package fr.rob.login.security.authentication.dev

import fr.rob.entities.AuthenticationProto
import fr.rob.login.security.authentication.AuthenticationProcess

class DevAuthenticationProcess : AuthenticationProcess() {

    override fun checkAuthentication(authMessage: Any): AuthenticationState {
        val userId = (authMessage as AuthenticationProto.DevAuthentication).userId

        if (userId == 0) {
            return AuthenticationState(false, error = ERROR_BAD_CREDENTIALS)
        }

        return AuthenticationState(true, userId)
    }
}
