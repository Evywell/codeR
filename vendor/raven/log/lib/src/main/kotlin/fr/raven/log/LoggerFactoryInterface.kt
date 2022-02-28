package fr.raven.log

interface LoggerFactoryInterface {

    fun create(name: String): LoggerInterface
}
