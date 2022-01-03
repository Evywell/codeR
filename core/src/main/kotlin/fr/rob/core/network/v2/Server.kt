package fr.rob.core.network.v2

import fr.rob.core.network.session.Session
import fr.rob.core.network.session.exception.SessionNotFoundException
import java.util.concurrent.ConcurrentHashMap

abstract class Server : ServerInterface {

    private val sessions: MutableMap<String, Session> = ConcurrentHashMap()

    override fun onNewConnection(id: String, session: Session) {
        registerSession(id, session)
    }

    override fun sessionFromIdentifier(identifier: String): Session {
        if (!sessions.containsKey(identifier)) {
            throw SessionNotFoundException(identifier)
        }

        return sessions[identifier]!!
    }

    override fun registerSession(identifier: String, session: Session) {
        if (sessions.containsKey(identifier)) {
            return // Session already registered
        }

        sessions[identifier] = session
    }

    fun getAllSessions(): Map<String, Session> = sessions
}
