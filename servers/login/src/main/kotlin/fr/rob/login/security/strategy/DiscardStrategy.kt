package fr.rob.login.security.strategy

import fr.rob.core.network.session.Session
import fr.rob.core.network.strategy.ServerStrategyInterface

class DiscardStrategy : ServerStrategyInterface {

    override fun authorizeNewConnection(): Boolean = false
    override fun authorizeSession(session: Session): Boolean = false
}
