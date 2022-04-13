package fr.rob.login.security.strategy

import fr.rob.core.network.strategy.ServerStrategyInterface
import fr.rob.core.network.v2.session.Session

class DiscardStrategy : ServerStrategyInterface {

    override fun authorizeNewConnection(): Boolean = false
    override fun authorizeSession(session: Session): Boolean = false
}
