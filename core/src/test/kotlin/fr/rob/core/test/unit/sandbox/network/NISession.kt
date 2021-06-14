package fr.rob.core.test.unit.sandbox.network

import fr.rob.core.network.session.Session

class NISession : Session() {

    init {
        socket = NISessionSocket()
    }

    companion object {
        fun buildAuthenticated(): NISession {
            val session = NISession()

            session.isAuthenticated = true
            session.userId = 1

            return session
        }
    }
}
