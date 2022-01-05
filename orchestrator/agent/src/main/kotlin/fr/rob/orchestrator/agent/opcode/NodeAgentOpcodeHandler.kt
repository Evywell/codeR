package fr.rob.orchestrator.agent.opcode

import fr.rob.core.log.LoggerInterface
import fr.rob.core.network.message.ResponseStackInterface
import fr.rob.orchestrator.agent.node.NodeAgentAdapterInterface
import fr.rob.orchestrator.agent.node.instance.NewInstanceOpcode
import fr.rob.orchestrator.shared.opcode.AGENT_REQUEST_CREATE_MAP_INSTANCE

class NodeAgentOpcodeHandler(
    private val adapter: NodeAgentAdapterInterface,
    responseStack: ResponseStackInterface,
    logger: LoggerInterface
) : OrchestratorAgentOpcodeHandler(responseStack, logger) {

    override fun initialize() {
        super.initialize()

        registerOpcode(AGENT_REQUEST_CREATE_MAP_INSTANCE, NewInstanceOpcode(adapter))
    }
}
