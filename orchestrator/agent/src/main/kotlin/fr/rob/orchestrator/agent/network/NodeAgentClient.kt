package fr.rob.orchestrator.agent.network

import fr.rob.core.log.LoggerInterface
import fr.rob.orchestrator.agent.node.NodeAgentAdapterInterface

class NodeAgentClient(private val adapter: NodeAgentAdapterInterface, private val logger: LoggerInterface) :
    AgentClient() {

    override fun createSession(): AgentSession = NodeAgentSession(adapter, responseStack, logger)
}
