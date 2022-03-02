package fr.rob.core.exception

import fr.raven.log.LoggerInterface

class ExceptionManager(val defaultLogger: LoggerInterface) {

    fun catchExceptions() {
        val handler = ExceptionHandler(this)

        Thread.setDefaultUncaughtExceptionHandler(handler)
    }

    class ExceptionHandler(private val em: ExceptionManager) : Thread.UncaughtExceptionHandler {

        override fun uncaughtException(thread: Thread, exception: Throwable) {
            em.defaultLogger.error("Exception thrown $exception with message \"${exception.message}\"\nStack trace: ${exception.stackTraceToString()}")
        }
    }
}
