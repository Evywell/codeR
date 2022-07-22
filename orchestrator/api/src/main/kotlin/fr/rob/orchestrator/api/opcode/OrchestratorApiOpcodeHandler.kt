package fr.rob.orchestrator.api.opcode

import fr.raven.log.LoggerInterface
import fr.rob.core.opcode.OpcodeHandler
import fr.rob.orchestrator.api.security.authentication.AuthenticationOpcode
import fr.rob.orchestrator.api.security.authentication.AuthenticationProcess
import fr.rob.orchestrator.shared.Orchestrator
import fr.rob.orchestrator.shared.opcode.API_AUTHENTICATE_SESSION

class OrchestratorApiOpcodeHandler(
    private val orchestratorEntity: Orchestrator,
    logger: LoggerInterface,
) :
    OpcodeHandler(logger) {

    override fun initialize() {
        registerOpcode(API_AUTHENTICATE_SESSION, AuthenticationOpcode(AuthenticationProcess(orchestratorEntity)))
    }
}
