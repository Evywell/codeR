package fr.rob.orchestrator.api.network

import fr.rob.core.network.session.Session
import fr.rob.orchestrator.api.opcode.OrchestratorApiOpcodeHandler

class OrchestratorSession(val handler: OrchestratorApiOpcodeHandler) : Session()
