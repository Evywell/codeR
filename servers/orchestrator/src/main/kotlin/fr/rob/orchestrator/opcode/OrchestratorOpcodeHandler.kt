package fr.rob.orchestrator.opcode

import fr.rob.core.log.LoggerInterface
import fr.rob.core.opcode.OpcodeHandler
import fr.rob.core.process.ProcessManager
import fr.rob.orchestrator.agent.AgentManagerProcess
import fr.rob.orchestrator.instances.InstanceManager
import fr.rob.orchestrator.instances.request.RequestNewInstanceProcess
import fr.rob.orchestrator.nodes.GameNodeManager
import fr.rob.orchestrator.nodes.NewGameNodeOpcode
import fr.rob.orchestrator.security.authentication.AuthenticationOpcode
import fr.rob.orchestrator.security.authentication.AuthenticationProcess

class OrchestratorOpcodeHandler(private val processManager: ProcessManager, logger: LoggerInterface) :
    OpcodeHandler(logger) {

    override fun initialize() {
        val authenticationProcess = processManager.getOrMakeProcess(AuthenticationProcess::class)
        val agentManagerProcess = processManager.getOrMakeProcess(AgentManagerProcess::class)

        registerOpcode(
            ServerOpcodeOrchestrator.AUTHENTICATE_SESSION,
            AuthenticationOpcode(authenticationProcess, agentManagerProcess)
        )

        registerOpcode(
            ServerOpcodeOrchestrator.NEW_GAME_NODE,
            NewGameNodeOpcode(
                processManager.getOrMakeProcess(GameNodeManager::class),
                processManager.getOrMakeProcess(InstanceManager::class),
                processManager.getOrMakeProcess(RequestNewInstanceProcess::class)
            )
        )
    }
}
