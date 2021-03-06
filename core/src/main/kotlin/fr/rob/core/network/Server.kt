package fr.rob.core.network

import fr.rob.core.network.session.Session
import fr.rob.core.network.session.exception.SessionNotFoundException
import fr.rob.core.network.strategy.NullServerStrategy
import fr.rob.core.network.strategy.ServerStrategyInterface

open class Server(var serverStrategy: ServerStrategyInterface = NullServerStrategy()) {

    private val sessions: MutableMap<Int, Session> = HashMap()

    fun sessionFromIdentifier(identifier: Int): Session {
        if (!sessions.containsKey(identifier)) {
            throw SessionNotFoundException(identifier)
        }

        return sessions[identifier]!!
    }

    open fun registerSession(identifier: Int, session: Session) {
        if (sessions.containsKey(identifier)) {
            return // Session already registered
        }

        sessions[identifier] = session
    }

    open fun start() {}
}
