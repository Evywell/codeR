package fr.rob.orchestrator.agent.network

import fr.rob.core.network.session.Session
import fr.rob.orchestrator.agent.opcode.OrchestratorAgentOpcodeHandler

abstract class AgentSession : Session() {

    val opcodeHandler: OrchestratorAgentOpcodeHandler

    init {
        opcodeHandler = createOpcodeHandler()
        opcodeHandler.initialize()
    }

    abstract fun createOpcodeHandler(): OrchestratorAgentOpcodeHandler
}
