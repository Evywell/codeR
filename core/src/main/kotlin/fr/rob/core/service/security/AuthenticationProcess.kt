package fr.rob.core.service.security

import fr.rob.core.network.v2.session.Session
import fr.rob.core.security.authentication.AuthenticationProcess as BaseAuthenticationProcess
import fr.rob.core.security.authentication.AuthenticationProcess.AuthenticationState as AuthenticationStateInterface

class AuthenticationProcess : BaseAuthenticationProcess() {
    override fun checkAuthentication(authMessage: Any): AuthenticationState = AuthenticationState(true)

    override fun authenticate(session: Session, authMessage: Any): AuthenticationState {
        session.isAuthenticated = true

        return checkAuthentication(authMessage)
    }

    data class AuthenticationState(
        override var isAuthenticated: Boolean,
        override var error: String? = null
    ) : AuthenticationStateInterface
}
