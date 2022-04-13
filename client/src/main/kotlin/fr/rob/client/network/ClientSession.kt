package fr.rob.client.network

import fr.rob.core.network.v2.session.Session
import fr.rob.core.network.v2.session.SessionSocketInterface

class ClientSession(socket: SessionSocketInterface) : Session(socket)
