package fr.rob.core.network.strategy

import fr.rob.core.network.v2.session.Session

interface ServerStrategyInterface {

    fun authorizeNewConnection(): Boolean
    fun authorizeSession(session: Session): Boolean
}
