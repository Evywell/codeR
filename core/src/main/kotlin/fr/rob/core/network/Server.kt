package fr.rob.core.network

import fr.rob.core.network.session.exception.SessionNotFoundException
import fr.rob.core.network.strategy.NullServerStrategy
import fr.rob.core.network.strategy.ServerStrategyInterface
import fr.rob.core.network.v2.session.Session
import java.util.concurrent.ConcurrentHashMap

open class Server(var serverStrategy: ServerStrategyInterface = NullServerStrategy()) {

    protected val sessions: MutableMap<String, Session> = ConcurrentHashMap()

    fun sessionFromIdentifier(identifier: String): Session {
        if (!sessions.containsKey(identifier)) {
            throw SessionNotFoundException(identifier)
        }

        return sessions[identifier]!!
    }

    open fun registerSession(identifier: String, session: Session) {
        if (sessions.containsKey(identifier)) {
            return // Session already registered
        }

        sessions[identifier] = session
    }

    open fun start() {}

    /**
     * Returns the readonly session map
     */
    fun getAllSessions(): Map<String, Session> = sessions
}
