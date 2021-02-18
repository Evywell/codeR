package fr.rob.test.sandbox.network

import fr.rob.game.domain.network.packet.Packet
import fr.rob.game.domain.network.session.Session

class NISession : Session() {

    override fun getIp(): String = ""

    override fun send(packet: Packet) {}

    override fun close() {
        isAuthenticated = false
    }
}
