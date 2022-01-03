package fr.rob.orchestrator.agent.opcode

import fr.rob.core.log.LoggerInterface
import fr.rob.core.network.message.ResponseStackInterface
import fr.rob.core.opcode.OpcodeHandler
import fr.rob.orchestrator.agent.actions.authentication.AuthenticationResultOpcode
import fr.rob.orchestrator.agent.node.NodeAgentAdapterInterface
import fr.rob.orchestrator.agent.node.instance.NewInstanceOpcode
import fr.rob.orchestrator.shared.opcode.AGENT_AUTHENTICATE_SESSION_RESULT
import fr.rob.orchestrator.shared.opcode.AGENT_REQUEST_CREATE_MAP_INSTANCE

class OrchestratorAgentOpcodeHandler(
    private val adapter: NodeAgentAdapterInterface,
    private val responseStack: ResponseStackInterface,
    logger: LoggerInterface
) :
    OpcodeHandler(logger) {

    override fun initialize() {
        registerOpcode(AGENT_AUTHENTICATE_SESSION_RESULT, AuthenticationResultOpcode(responseStack))

        registerOpcode(AGENT_REQUEST_CREATE_MAP_INSTANCE, NewInstanceOpcode(adapter))
    }
}
