package fr.rob.core.log

import org.apache.logging.log4j.Logger

class Logger(val logger: Logger) : LoggerInterface {

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
