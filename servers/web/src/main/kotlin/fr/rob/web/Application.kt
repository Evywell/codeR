package fr.rob.web

import fr.rob.core.AbstractModule
import fr.rob.core.BaseApplication
import fr.rob.core.ENV_DEV
import fr.rob.core.config.Config
import fr.rob.core.initiator.Initiator
import fr.rob.core.log.LoggerFactory
import fr.rob.core.network.Server

class Application : BaseApplication(ENV_DEV, LoggerFactory.create("web")) {

    override fun registerModules(modules: MutableList<AbstractModule>) {}

    override fun registerInitiatorTasks(initiator: Initiator) {}
    override fun registerConfigHandlers(config: Config) {}
    override fun createServer(): Server? = null
}
