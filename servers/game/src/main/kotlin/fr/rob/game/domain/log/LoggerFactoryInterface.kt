package fr.rob.game.domain.log

interface LoggerFactoryInterface {

    fun create(name: String): LoggerInterface
}
