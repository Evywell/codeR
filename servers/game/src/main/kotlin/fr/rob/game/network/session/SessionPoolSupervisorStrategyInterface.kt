package fr.rob.game.network.session

import fr.rob.core.network.v2.session.Session

interface SessionPoolSupervisorStrategyInterface {

    fun addSessionInPool(session: Session): Boolean
}
