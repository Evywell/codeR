package fr.rob.orchestrator.agent.network

import fr.rob.core.log.LoggerInterface
import fr.rob.core.network.message.ResponseStackInterface
import fr.rob.orchestrator.agent.node.NodeAgentAdapterInterface
import fr.rob.orchestrator.agent.opcode.NodeAgentOpcodeHandler
import fr.rob.orchestrator.agent.opcode.OrchestratorAgentOpcodeHandler

class NodeAgentSession(
    private val adapter: NodeAgentAdapterInterface,
    private val responseStack: ResponseStackInterface,
    private val logger: LoggerInterface
) : AgentSession() {

    override fun createOpcodeHandler(): OrchestratorAgentOpcodeHandler =
        NodeAgentOpcodeHandler(adapter, responseStack, logger)
}
