package fr.rob.core.security.authentication

import fr.raven.log.LoggerInterface
import fr.rob.core.log.LoggableException

class UnauthenticatedException(message: String, logger: LoggerInterface) : LoggableException(logger, message)
