package fr.rob.orchestrator.opcode

import fr.rob.core.log.LoggerInterface
import fr.rob.core.opcode.OpcodeHandler
import fr.rob.core.process.ProcessManager
import fr.rob.orchestrator.security.authentication.AuthenticationOpcode
import fr.rob.orchestrator.security.authentication.AuthenticationProcess

class OrchestratorOpcodeHandler(private val processManager: ProcessManager, logger: LoggerInterface) :
    OpcodeHandler(logger) {

    override fun initialize() {
        val authenticationProcess = processManager.getOrMakeProcess(AuthenticationProcess::class)

        registerOpcode(ServerOpcodeOrchestrator.AUTHENTICATE_SESSION, AuthenticationOpcode(authenticationProcess))
    }
}
