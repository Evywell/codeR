package fr.rob.game.domain.log

open class LoggableException(val logger: LoggerInterface, message: String) : Exception(message)
