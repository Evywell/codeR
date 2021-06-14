package fr.rob.login.security.authentication

import fr.rob.core.network.session.Session

abstract class AuthenticationProcess {

    protected abstract fun checkAuthentication(authMessage: Any): AuthenticationState

    fun authenticate(session: Session, authMessage: Any): AuthenticationState {
        val state = checkAuthentication(authMessage)

        if (!state.isAuthenticated) {
            return state
        }

        session.isAuthenticated = true
        session.userId = state.userId

        return state
    }

    companion object {
        const val ERROR_BAD_CREDENTIALS = "bad_credentials"
    }

    data class AuthenticationState(
        var isAuthenticated: Boolean,
        var userId: Int? = null,
        var error: String? = null,
        var accountName: String? = null
    )
}
