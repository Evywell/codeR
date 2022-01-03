package fr.rob.core.network.v2

import fr.rob.core.network.Packet
import fr.rob.core.network.session.Session

interface ServerInterface {

    fun onNewConnection(id: String, session: Session)
    fun onPacketReceived(session: Session, packet: Packet)

    fun createSession(): Session
    fun sessionFromIdentifier(identifier: String): Session
    fun registerSession(identifier: String, session: Session)

    fun start(unitOfWork: ServerProcessInterface) {
        unitOfWork.start()
    }
}
