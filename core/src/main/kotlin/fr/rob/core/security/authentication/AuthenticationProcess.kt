package fr.rob.core.security.authentication

import fr.rob.core.network.v2.session.Session

abstract class AuthenticationProcess {

    protected abstract fun checkAuthentication(authMessage: Any): AuthenticationState

    abstract fun authenticate(session: Session, authMessage: Any): AuthenticationState

    companion object {
        const val ERROR_BAD_CREDENTIALS = "bad_credentials"
    }

    interface AuthenticationState {
        var isAuthenticated: Boolean
        var error: String?
    }
}
