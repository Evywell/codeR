package fr.rob.orchestrator.security

import fr.rob.core.AbstractModule
import fr.rob.core.process.ProcessManager
import fr.rob.orchestrator.security.authentication.AuthenticationProcess
import fr.rob.shared.orchestrator.Orchestrator

class SecurityModule(
    private val processManager: ProcessManager
) :
    AbstractModule() {

    override fun boot() {
        processManager.registerProcess(AuthenticationProcess::class) {
            AuthenticationProcess(processManager.getOrMakeProcess(Orchestrator::class))
        }
    }
}
