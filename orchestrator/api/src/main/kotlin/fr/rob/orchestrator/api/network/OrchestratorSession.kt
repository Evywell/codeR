package fr.rob.orchestrator.api.network

import fr.rob.core.network.v2.session.Session
import fr.rob.core.network.v2.session.SessionSocketInterface
import fr.rob.orchestrator.api.opcode.OrchestratorApiOpcodeHandler

class OrchestratorSession(val handler: OrchestratorApiOpcodeHandler, socket: SessionSocketInterface) : Session(socket)
