package fr.rob.login.security.authentication

import fr.rob.core.network.session.Session

abstract class AuthenticationProcess {

    protected abstract fun checkAuthentication(authMessage: Any): Boolean
    protected abstract fun getUserId(): Int

    fun authenticate(session: Session, authMessage: Any): Boolean {
        if (!checkAuthentication(authMessage)) {
            return false
        }

        session.isAuthenticated = true
        session.userId = getUserId()

        return true
    }

}
