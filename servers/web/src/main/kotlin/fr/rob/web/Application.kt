package fr.rob.web

import fr.rob.core.AbstractModule
import fr.rob.core.BaseApplication
import fr.rob.core.ENV_DEV
import fr.rob.core.config.Config
import fr.rob.core.config.hashmap.HashMapConfigLoader
import fr.rob.core.event.EventManager
import fr.rob.core.initiator.Initiator
import fr.rob.core.log.LoggerFactory

class Application : BaseApplication(ENV_DEV, LoggerFactory.create("web"), HashMapConfigLoader(), EventManager()) {

    override fun registerModules(modules: MutableList<AbstractModule>) {}

    override fun registerInitiatorTasks(initiator: Initiator) {}
    override fun registerConfigHandlers(config: Config) {}
}
