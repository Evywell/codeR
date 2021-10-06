package fr.rob.game.network.session

import fr.rob.core.network.session.Session

interface SessionPoolSupervisorStrategyInterface {

    fun addSessionInPool(session: Session): Boolean
}
