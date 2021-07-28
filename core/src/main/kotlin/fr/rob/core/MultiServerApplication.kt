package fr.rob.core

import fr.rob.core.config.ConfigLoaderInterface
import fr.rob.core.event.EventManagerInterface
import fr.rob.core.log.LoggerInterface

abstract class MultiServerApplication(
    env: String,
    logger: LoggerInterface,
    configLoader: ConfigLoaderInterface,
    eventManager: EventManagerInterface
) : BaseApplication(env, logger, configLoader, eventManager)
