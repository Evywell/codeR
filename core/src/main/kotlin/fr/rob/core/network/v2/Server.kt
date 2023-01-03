package fr.rob.core.network.v2

import fr.rob.core.network.v2.session.MapSessionHolder
import fr.rob.core.network.v2.session.Session

abstract class Server<T> : ServerInterface<T> {

    private val sessionHolder = MapSessionHolder()

    override fun onNewConnection(id: String, session: Session) {
        registerSession(id, session)
    }

    override fun onConnectionClosed(session: Session) {}

    override fun sessionFromIdentifier(identifier: String): Session = sessionHolder.sessionFromIdentifier(identifier)

    override fun registerSession(identifier: String, session: Session) {
        sessionHolder.registerSession(identifier, session)
    }

    fun getAllSessions(): Map<String, Session> = sessionHolder.getAllSessions()
}
