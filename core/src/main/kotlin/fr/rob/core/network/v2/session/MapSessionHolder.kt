package fr.rob.core.network.v2.session

import fr.rob.core.network.session.exception.SessionNotFoundException
import java.util.concurrent.ConcurrentHashMap

class MapSessionHolder : SessionHolderInterface {
    private val sessions: MutableMap<String, Session> = ConcurrentHashMap()

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

    override fun getAllSessions(): Map<String, Session> = sessions
}
