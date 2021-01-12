package fr.rob.game.domain.authentication

import fr.rob.game.domain.log.LoggableException
import fr.rob.game.domain.log.LoggerInterface

class UnauthenticatedException(message: String, logger: LoggerInterface) : LoggableException(logger, message)
