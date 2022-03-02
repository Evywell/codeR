package fr.rob.core.log

import fr.raven.log.LoggerInterface

open class LoggableException(val logger: LoggerInterface, message: String) : Exception(message)
