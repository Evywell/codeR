package fr.rob.gateway.network

import fr.rob.core.network.v2.session.Session
import fr.rob.core.network.v2.session.SessionSocketInterface

class GatewaySession(socket: SessionSocketInterface) : Session(socket)
