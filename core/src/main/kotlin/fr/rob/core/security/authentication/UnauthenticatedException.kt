package fr.rob.core.security.authentication

import fr.rob.core.log.LoggableException
import fr.rob.core.log.LoggerInterface

class UnauthenticatedException(message: String, logger: LoggerInterface) : LoggableException(logger, message)
