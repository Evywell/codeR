package fr.rob.login.test.feature.service.exception

import kotlin.system.exitProcess

object ExceptionHolder {

    fun throwException(e: Exception) {
        println(e.stackTraceToString())
        exitProcess(1)
    }
}
