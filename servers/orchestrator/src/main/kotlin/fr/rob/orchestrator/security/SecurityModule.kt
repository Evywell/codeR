package fr.rob.orchestrator.security

import fr.rob.core.AbstractModule
import fr.rob.core.process.ProcessManager
import fr.rob.orchestrator.security.authentication.AuthenticationProcess
import fr.rob.shared.orchestrator.OrchestratorRepositoryInterface
import fr.rob.shared.orchestrator.config.OrchestratorConfigHandler.OrchestratorConfig

class SecurityModule(
    private val orchestratorConfig: OrchestratorConfig,
    private val orchestratorRepository: OrchestratorRepositoryInterface,
    private val processManager: ProcessManager
) :
    AbstractModule() {

    override fun boot() {
        val orchestrator = orchestratorRepository.getOrchestratorById(orchestratorConfig.id)

        processManager.registerProcess(AuthenticationProcess::class) {
            AuthenticationProcess(orchestrator!!)
        }
    }
}
