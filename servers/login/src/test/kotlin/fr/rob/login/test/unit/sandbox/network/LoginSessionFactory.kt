package fr.rob.login.test.unit.sandbox.network

import fr.rob.core.test.unit.sandbox.network.NISessionSocket
import fr.rob.login.network.LoginSession

class LoginSessionFactory {

    companion object {
        fun buildSession(): LoginSession {
            val session = LoginSession()

            session.socket = NISessionSocket()

            return session
        }

        fun buildAuthenticatedSession(): LoginSession {
            val session = buildSession()

            session.isAuthenticated = true
            session.userId = 1

            return session
        }
    }
}
