package fr.rob.core.test.unit.sandbox.network

import fr.rob.core.network.Packet
import fr.rob.core.network.session.SessionSocketInterface

class NISessionSocket : SessionSocketInterface {

    override fun getIp(): String = ""

    override fun send(packet: Packet) { }

    override fun close() { }
}
