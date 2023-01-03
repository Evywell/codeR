package fr.rob.core.network.v2

import fr.rob.core.network.v2.session.Session
import fr.rob.core.network.v2.session.SessionSocketInterface

interface ServerInterface<T> {

    fun onNewConnection(id: String, session: Session)
    fun onConnectionClosed(session: Session)
    fun onPacketReceived(session: Session, packet: T)

    fun createSession(socket: SessionSocketInterface): Session
    fun sessionFromIdentifier(identifier: String): Session
    fun registerSession(identifier: String, session: Session)

    fun start(unitOfWork: ServerProcessInterface) {
        unitOfWork.start()
    }
}
