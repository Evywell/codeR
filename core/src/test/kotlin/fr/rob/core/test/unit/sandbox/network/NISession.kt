package fr.rob.core.test.unit.sandbox.network

import fr.rob.core.network.Packet
import fr.rob.core.network.session.Session

class NISession : Session() {

    override fun getIp(): String = ""

    override fun send(packet: Packet) {}

    override fun close() {
        isAuthenticated = false
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
