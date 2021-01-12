package fr.rob.game.domain.network

import fr.rob.game.domain.log.LoggerFactoryInterface
import fr.rob.game.domain.network.exception.SessionNotFoundException
import fr.rob.game.domain.network.session.Session
import fr.rob.game.domain.opcode.ClientOpcodeHandler

open class GameServer(val name: String, loggerFactory: LoggerFactoryInterface) {

    val logger = loggerFactory.create(name)
    val clientOpcodeHandler = ClientOpcodeHandler(loggerFactory.create("OPCODE"))

    private val sessions: MutableMap<Int, Session> = HashMap()

    fun sessionFromIdentifier(identifier: Int): Session {
        if (!sessions.containsKey(identifier)) {
            throw SessionNotFoundException(identifier)
        }

        return sessions[identifier]!!
    }

    fun registerSession(identifier: Int, session: Session) {
        if (sessions.containsKey(identifier)) {
            return // Session already registered
        }

        sessions[identifier] = session
    }

    open fun start() = Unit
}
