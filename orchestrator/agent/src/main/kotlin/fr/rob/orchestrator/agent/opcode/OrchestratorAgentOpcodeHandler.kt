package fr.rob.orchestrator.agent.opcode

import fr.rob.core.log.LoggerInterface
import fr.rob.core.network.message.ResponseStackInterface
import fr.rob.core.opcode.OpcodeHandler
import fr.rob.orchestrator.agent.actions.authentication.AuthenticationResultOpcode
import fr.rob.orchestrator.shared.opcode.AGENT_AUTHENTICATE_SESSION_RESULT

open class OrchestratorAgentOpcodeHandler(
    private val responseStack: ResponseStackInterface,
    logger: LoggerInterface
) :
    OpcodeHandler(logger) {

    override fun initialize() {
        registerOpcode(AGENT_AUTHENTICATE_SESSION_RESULT, AuthenticationResultOpcode(responseStack))
    }
}
