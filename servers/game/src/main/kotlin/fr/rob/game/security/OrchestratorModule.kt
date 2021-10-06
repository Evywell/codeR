package fr.rob.game.security

import fr.rob.core.AbstractModule
import fr.rob.core.log.LoggerFactoryInterface
import fr.rob.core.misc.Network
import fr.rob.core.process.ProcessManager
import fr.rob.orchestrator.agent.AgentFactory
import fr.rob.orchestrator.agent.NodeAgent
import fr.rob.shared.orchestrator.OrchestratorRepositoryInterface
import fr.rob.shared.orchestrator.config.OrchestratorConfigHandler

class OrchestratorModule(
    private val orchestratorRepository: OrchestratorRepositoryInterface,
    private val orchestratorConfig: OrchestratorConfigHandler.OrchestratorConfig,
    private val processManager: ProcessManager,
    private val loggerFactory: LoggerFactoryInterface
) : AbstractModule() {

    override fun boot() {
        val orchestrator = orchestratorRepository.getOrchestratorById(orchestratorConfig.id)
        val agentFactory = AgentFactory(orchestrator!!, loggerFactory.create("agent"))
        val address = Network.getAddress(orchestrator.address)

        processManager.registerProcess(NodeAgent::class) {
            agentFactory.createNodeAgent(address.ip, address.port)
        }
    }
}
