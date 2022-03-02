package fr.rob.orchestrator.api.opcode

import fr.raven.log.LoggerInterface
import fr.rob.core.opcode.OpcodeHandler
import fr.rob.core.process.ProcessManager
import fr.rob.orchestrator.api.composer.RequestComposer
import fr.rob.orchestrator.api.instance.DefaultInstancesRepositoryInterface
import fr.rob.orchestrator.api.instance.InstanceManager
import fr.rob.orchestrator.api.node.NewGameNodeOpcode
import fr.rob.orchestrator.api.node.NodeManager
import fr.rob.orchestrator.api.security.authentication.AuthenticationOpcode
import fr.rob.orchestrator.api.security.authentication.AuthenticationProcess
import fr.rob.orchestrator.shared.Orchestrator
import fr.rob.orchestrator.shared.opcode.API_AUTHENTICATE_SESSION
import fr.rob.orchestrator.shared.opcode.API_NEW_GAME_NODE

class OrchestratorApiOpcodeHandler(
    private val orchestratorEntity: Orchestrator,
    logger: LoggerInterface,
    private val processManager: ProcessManager
) :
    OpcodeHandler(logger) {

    override fun initialize() {
        val nodeManager = processManager.getOrMakeProcess(NodeManager::class)
        val defaultInstancesRepository = processManager.getOrMakeProcess(DefaultInstancesRepositoryInterface::class)
        val instanceManager = processManager.getOrMakeProcess(InstanceManager::class)
        val requestComposer = processManager.getOrMakeProcess(RequestComposer::class)

        registerOpcode(API_AUTHENTICATE_SESSION, AuthenticationOpcode(AuthenticationProcess(orchestratorEntity)))

        registerOpcode(
            API_NEW_GAME_NODE,
            NewGameNodeOpcode(nodeManager, defaultInstancesRepository, instanceManager, requestComposer)
        )
    }
}
