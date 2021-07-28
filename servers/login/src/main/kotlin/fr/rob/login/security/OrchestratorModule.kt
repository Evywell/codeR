package fr.rob.login.security

import fr.rob.client.network.Client
import fr.rob.core.AbstractModule
import fr.rob.core.misc.Network
import fr.rob.core.process.ProcessManager
import fr.rob.orchestrator.agent.AbstractAgent
import fr.rob.orchestrator.agent.SingleJobAgent
import fr.rob.shared.orchestrator.OrchestratorRepositoryInterface
import fr.rob.shared.orchestrator.config.OrchestratorConfigHandler

class OrchestratorModule(
    private val orchestratorRepository: OrchestratorRepositoryInterface,
    private val orchestratorConfig: OrchestratorConfigHandler.OrchestratorConfig,
    private val processManager: ProcessManager
) : AbstractModule() {

    override fun boot() {
        val orchestrator = orchestratorRepository.getOrchestratorById(orchestratorConfig.id)
        val address = Network.getIpAndPort(orchestrator!!.address)

        processManager.registerProcess(AbstractAgent::class) {
            SingleJobAgent(Client(address.ip, address.port), orchestrator.token, "login")
        }
    }
}
