package fr.rob.game.domain.network.session

import fr.rob.core.network.session.Session

class StupidSessionPoolSupervisorStrategy(private val sessionPools: Array<SessionPool>) :
    SessionPoolSupervisorStrategy {

    private var currentIndex: Int = 0

    override fun addSessionInPool(session: Session): Boolean {
        val index = getNextIndex()

        if (sessionPools[index].isFull()) {
            return false
        }

        sessionPools[index].addSession(session)

        return true
    }

    private fun getNextIndex(): Int {
        currentIndex++

        if (currentIndex >= sessionPools.size) {
            currentIndex = 0
        }

        return currentIndex
    }
}
