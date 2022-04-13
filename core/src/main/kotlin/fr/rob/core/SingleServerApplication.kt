package fr.rob.core

import fr.raven.log.LoggerInterface
import fr.rob.core.config.ConfigLoaderInterface
import fr.rob.core.event.EventManagerInterface
import fr.rob.core.network.v2.Server

abstract class SingleServerApplication<T>(
    env: String,
    logger: LoggerInterface,
    configLoader: ConfigLoaderInterface,
    eventManager: EventManagerInterface
) : BaseApplication(env, logger, configLoader, eventManager) {

    protected lateinit var server: Server<T>

    override fun initDependencies() {
        super.initDependencies()

        server = createServer()
    }

    abstract fun createServer(): Server<T>
}
