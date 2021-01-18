package fr.rob.game.domain.security.authentication

import fr.rob.game.domain.network.session.Session

abstract class AuthenticationProcess {

    protected abstract fun checkAuthentication(): Boolean
    protected abstract fun getUserId(): Int

    fun authenticate(session: Session): Boolean {
        if (!checkAuthentication()) {
            return false
        }

        session.isAuthenticated = true
        session.userId = getUserId()

        return true
    }

}
