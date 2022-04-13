package fr.rob.game.network.session

import fr.rob.core.network.v2.session.Session
import fr.rob.game.network.session.SessionPool.Companion.DEFAULT_MAX_SESSIONS

class SessionPoolManager(private val maxSessionPerPool: Int = DEFAULT_MAX_SESSIONS) {

    private val sessionPools = Array(MAX_SESSION_POOLS) { _ -> SessionPool(maxSessionPerPool) }
    private val strategy: SessionPoolSupervisorStrategyInterface = StupidSessionPoolSupervisorStrategy(sessionPools)

    fun addSession(session: Session): Boolean = strategy.addSessionInPool(session)

    companion object {
        const val MAX_SESSION_POOLS = 4
    }
}
