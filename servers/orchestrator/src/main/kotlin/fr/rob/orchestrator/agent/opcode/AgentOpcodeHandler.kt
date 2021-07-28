package fr.rob.orchestrator.agent.opcode

import fr.rob.core.log.LoggerInterface
import fr.rob.core.opcode.OpcodeHandler
import fr.rob.orchestrator.agent.authentication.AuthenticationResultOpcode
import fr.rob.orchestrator.opcode.AgentOpcodeOrchestrator.Companion.AUTHENTICATE_SESSION_RESULT

open class AgentOpcodeHandler(logger: LoggerInterface) : OpcodeHandler(logger) {

    override fun initialize() {
        registerOpcode(AUTHENTICATE_SESSION_RESULT, AuthenticationResultOpcode())
    }
}
