package fr.rob.web

import fr.raven.log.LoggerInterface
import fr.rob.core.AbstractModule
import fr.rob.core.BaseApplication
import fr.rob.core.ENV_DEV
import fr.rob.core.config.Config
import fr.rob.core.config.hashmap.HashMapConfigLoader
import fr.rob.core.event.EventManager
import fr.rob.core.initiator.Initiator

class Application(logger: LoggerInterface) :
    BaseApplication(ENV_DEV, logger, HashMapConfigLoader(), EventManager()) {

    override fun registerModules(modules: MutableList<AbstractModule>) {}

    override fun registerInitiatorTasks(initiator: Initiator) {}
    override fun registerConfigHandlers(config: Config) {}
}
