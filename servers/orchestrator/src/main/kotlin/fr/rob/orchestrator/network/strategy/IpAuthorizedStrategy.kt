package fr.rob.orchestrator.network.strategy

import fr.rob.core.network.session.Session
import fr.rob.core.network.strategy.ServerStrategyInterface

class IpAuthorizedStrategy(private val ipList: Array<String>) : ServerStrategyInterface {

    override fun authorizeNewConnection(): Boolean = true

    override fun authorizeSession(session: Session): Boolean = ipList.contains(session.getIp())
}
