package fr.rob.core

import fr.raven.log.LoggerInterface
import fr.rob.core.config.ConfigLoaderInterface
import fr.rob.core.event.EventManagerInterface

abstract class MultiServerApplication(
    env: String,
    logger: LoggerInterface,
    configLoader: ConfigLoaderInterface,
    eventManager: EventManagerInterface
) : BaseApplication(env, logger, configLoader, eventManager)
