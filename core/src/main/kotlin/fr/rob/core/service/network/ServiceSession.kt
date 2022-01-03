package fr.rob.core.service.network

import fr.rob.core.network.session.Session
import fr.rob.core.opcode.OpcodeHandler

class ServiceSession(val handler: OpcodeHandler) : Session()
