package fr.rob.core.test.unit.sandbox.network

import fr.rob.core.network.v2.session.Session

class NISession : Session(NISessionSocket()) {

    companion object {
        fun buildAuthenticated(): NISession {
            val session = NISession()

            session.isAuthenticated = true
            session.accountId = 1

            return session
        }
    }
}
