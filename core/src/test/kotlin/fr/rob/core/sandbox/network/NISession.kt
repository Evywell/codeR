package fr.rob.core.sandbox.network

import fr.rob.core.network.Packet
import fr.rob.core.network.session.Session

class NISession : Session() {

    override fun getIp(): String = ""

    override fun send(packet: Packet) {}

    override fun close() {
        isAuthenticated = false
    }
}
