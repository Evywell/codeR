package fr.rob.game.domain.network.session

import fr.rob.core.network.session.Session

interface SessionPoolSupervisorStrategy {

    fun addSessionInPool(session: Session): Boolean
}
