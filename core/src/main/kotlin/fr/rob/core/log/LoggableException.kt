package fr.rob.core.log

open class LoggableException(val logger: LoggerInterface, message: String) : Exception(message)
