package fr.rob.core.test.unit.sandbox.network

import fr.rob.core.network.Packet
import fr.rob.core.network.v2.Server
import fr.rob.core.network.v2.session.Session
import fr.rob.core.network.v2.session.SessionSocketInterface

class NIServer : Server<Packet>() {
    override fun onPacketReceived(session: Session, packet: Packet) {}

    override fun createSession(socket: SessionSocketInterface): Session = NISession()
}
