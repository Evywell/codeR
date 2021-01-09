package fr.rob.game.domain.network

import fr.rob.game.domain.log.LoggerInterface
import fr.rob.game.domain.opcode.ClientOpcodeHandler
import fr.rob.game.domain.network.exception.SessionNotFoundException

open class GameServer(logger: LoggerInterface) {

    val clientOpcodeHandler = ClientOpcodeHandler(logger)

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
}