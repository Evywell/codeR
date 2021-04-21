package fr.rob.login.security.authentication

import fr.rob.core.network.session.Session

abstract class AuthenticationProcess {

    var error: String? = null
        private set

    protected abstract fun checkAuthentication(authMessage: Any): Boolean
    protected abstract fun getUserId(): Int

    fun authenticate(session: Session, authMessage: Any): Boolean {
        if (!checkAuthentication(authMessage)) {
            this.error = ERROR_BAD_CREDENTIALS

            return false
        }

        session.isAuthenticated = true
        session.userId = getUserId()

        return true
    }

    companion object {
        const val ERROR_BAD_CREDENTIALS = "bad_credentials"
    }

}
