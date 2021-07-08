package fr.rob.login.security.authentication

import fr.rob.core.network.session.Session
import fr.rob.login.security.account.AccountProcess

abstract class AuthenticationProcess(private val accountProcess: AccountProcess) {

    protected abstract fun checkAuthentication(authMessage: Any): AuthenticationState

    fun authenticate(session: Session, authMessage: Any): AuthenticationState {
        val state = checkAuthentication(authMessage)

        if (!state.isAuthenticated) {
            return state
        }

        val account = accountProcess.retrieve(state.userId!!)

        if (account != null) {
            // Check banned or locked
            val isBanned = account.isBanned
            val isLocked = account.isLocked

            if (isBanned || isLocked) {
                return AuthenticationState(false, error = if (isBanned) ERROR_BANNED_ACCOUNT else ERROR_LOCKED_ACCOUNT)
            }
        }

        session.isAuthenticated = true
        session.userId = state.userId

        return state
    }

    companion object {
        const val ERROR_BAD_CREDENTIALS = "bad_credentials"
        const val ERROR_BANNED_ACCOUNT = "err_banned_account"
        const val ERROR_LOCKED_ACCOUNT = "err_locked_account"
    }

    data class AuthenticationState(
        var isAuthenticated: Boolean,
        var userId: Int? = null,
        var error: String? = null,
        var accountName: String? = null
    )
}
