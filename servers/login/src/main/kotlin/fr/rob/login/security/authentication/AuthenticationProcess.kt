package fr.rob.login.security.authentication

import fr.rob.core.network.v2.session.Session
import fr.rob.login.security.account.AccountProcess
import fr.rob.core.security.authentication.AuthenticationProcess as BaseAuthenticationProcess

abstract class AuthenticationProcess(private val accountProcess: AccountProcess) : BaseAuthenticationProcess() {

    override fun authenticate(session: Session, authMessage: Any): LoginAuthenticationState {
        val state = checkAuthentication(authMessage) as LoginAuthenticationState

        if (!state.isAuthenticated) {
            return state
        }

        val account = accountProcess.retrieve(state.accountId!!)

        if (account != null) {
            // Check banned or locked
            val isBanned = account.isBanned
            val isLocked = account.isLocked

            if (isBanned || isLocked) {
                return LoginAuthenticationState(
                    false,
                    error = if (isBanned) ERROR_BANNED_ACCOUNT else ERROR_LOCKED_ACCOUNT
                )
            }
        }

        session.isAuthenticated = true
        session.accountId = state.accountId

        return state
    }

    companion object {
        const val ERROR_BANNED_ACCOUNT = "err_banned_account"
        const val ERROR_LOCKED_ACCOUNT = "err_locked_account"
    }

    data class LoginAuthenticationState(
        override var isAuthenticated: Boolean,
        var accountId: Int? = null,
        override var error: String? = null,
        var accountName: String? = null
    ) : AuthenticationState
}
