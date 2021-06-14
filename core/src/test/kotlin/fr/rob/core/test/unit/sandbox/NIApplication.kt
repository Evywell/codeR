package fr.rob.core.test.unit.sandbox

import fr.rob.core.AbstractModule
import fr.rob.core.BaseApplication
import fr.rob.core.ENV_TEST
import fr.rob.core.config.Config
import fr.rob.core.initiator.Initiator
import fr.rob.core.network.Server
import fr.rob.core.test.unit.sandbox.log.NILogger
import fr.rob.core.test.unit.sandbox.network.NIServer

class NIApplication : BaseApplication(ENV_TEST, NILogger()) {
    override fun registerModules(modules: MutableList<AbstractModule>) {}

    override fun registerInitiatorTasks(initiator: Initiator) {}
    override fun registerConfigHandlers(config: Config) {}

    override fun createServer(): Server = NIServer()
}
