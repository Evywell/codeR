package fr.rob.core.network.strategy

import fr.rob.core.network.v2.session.Session

class NullServerStrategy : ServerStrategyInterface {

    override fun authorizeNewConnection(): Boolean = true
    override fun authorizeSession(session: Session): Boolean = true
}
