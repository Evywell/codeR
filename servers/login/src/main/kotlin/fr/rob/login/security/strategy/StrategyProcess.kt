package fr.rob.login.security.strategy

import fr.rob.core.network.Server
import fr.rob.login.network.LoginSession

class StrategyProcess(private val server: Server) {

    fun changeServerStrategy(session: LoginSession, newStrategy: String) {
        session.isOperatorOrThrowException()

        val strategy = StrategyFactory.createStrategy(newStrategy)
        server.serverStrategy = strategy
    }
}
