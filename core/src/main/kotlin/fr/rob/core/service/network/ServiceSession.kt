package fr.rob.core.service.network

import fr.rob.core.network.v2.session.Session
import fr.rob.core.network.v2.session.SessionSocketInterface
import fr.rob.core.opcode.OpcodeHandler

class ServiceSession(val handler: OpcodeHandler, socket: SessionSocketInterface) : Session(socket)
