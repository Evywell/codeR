package fr.rob.core.log

interface LoggerFactoryInterface {

    fun create(name: String): LoggerInterface
}
