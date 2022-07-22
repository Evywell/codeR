package fr.rob.gateway.network

import fr.rob.core.network.v2.session.Session
import fr.rob.core.network.v2.session.SessionSocketInterface
import fr.rob.gateway.extension.game.GameNode

class GatewaySession(socket: SessionSocketInterface) : Session(socket) {
    lateinit var id: String
    var isAuthenticatedToGameNode = false
    var currentGameNode: GameNode? = null

    var characterInQueue: Int? = null

    fun authenticate(accountId: Int) {
        this.accountId = accountId
        isAuthenticated = true
    }
}
