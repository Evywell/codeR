package fr.rob.login.test.unit.sandbox.network

import fr.rob.core.test.unit.sandbox.network.NISessionSocket
import fr.rob.login.network.LoginSession

class LoginSessionFactory {

    companion object {
        fun buildSession(): LoginSession = LoginSession(NISessionSocket())

        fun buildAuthenticatedSession(): LoginSession {
            val session = buildSession()

            session.isAuthenticated = true
            session.userId = 1

            return session
        }

        fun buildAuthenticatedSpyingSession(): SpyingLoginSession {
            val session = SpyingLoginSession(SpyingLoginSocket())

            session.isAuthenticated = true
            session.userId = 1

            return session
        }
    }
}
