package fr.rob.gateway.network

import fr.rob.core.network.v2.session.Session
import fr.rob.core.network.v2.session.SessionSocketInterface
import fr.rob.gateway.store.Store

class GatewaySession(socket: SessionSocketInterface) : Session(socket) {
    val store = Store()
}
