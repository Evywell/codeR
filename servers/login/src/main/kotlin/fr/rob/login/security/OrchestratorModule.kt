package fr.rob.login.security

import fr.rob.core.AbstractModule
import fr.rob.core.log.LoggerFactoryInterface
import fr.rob.core.misc.Network
import fr.rob.core.process.ProcessManager

class OrchestratorModule(
    private val processManager: ProcessManager,
    private val loggerFactory: LoggerFactoryInterface
) : AbstractModule() {

    override fun boot() {
    }
}
