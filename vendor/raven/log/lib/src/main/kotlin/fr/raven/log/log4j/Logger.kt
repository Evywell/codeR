package fr.raven.log.log4j

import fr.raven.log.LoggerInterface
import org.apache.logging.log4j.Logger

class Logger(private val logger: Logger) : LoggerInterface {

    override fun debug(message: String, vararg parameters: Any) {
        if (logger.isDebugEnabled) {
            logger.debug(message, parameters)
        }
    }

    override fun error(message: String, vararg parameters: Any) {
        logger.error(message, parameters)
    }

    override fun info(message: String, vararg parameters: Any) {
        logger.info(message, parameters)
    }

    override fun warning(message: String, vararg parameters: Any) {
        logger.warn(message, parameters)
    }
}
