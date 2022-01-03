package fr.rob.orchestrator.agent.network

import fr.rob.core.log.LoggerInterface
import fr.rob.core.network.message.ResponseStackInterface
import fr.rob.core.network.session.Session
import fr.rob.orchestrator.agent.node.NodeAgentAdapterInterface
import fr.rob.orchestrator.agent.opcode.OrchestratorAgentOpcodeHandler

class AgentSession(adapter: NodeAgentAdapterInterface, responseStack: ResponseStackInterface, logger: LoggerInterface) :
    Session() {

    val opcodeHandler = OrchestratorAgentOpcodeHandler(adapter, responseStack, logger)

    init {
        opcodeHandler.initialize()
    }
}
