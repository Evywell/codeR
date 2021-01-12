package fr.rob.test

import fr.rob.game.domain.log.LoggerFactoryInterface
import fr.rob.game.domain.log.LoggerInterface
import fr.rob.test.sandbox.log.NILogger
import fr.rob.test.sandbox.log.NILoggerFactory

open class BaseTest {

    val logger: LoggerInterface = NILogger()
    val loggerFactory: LoggerFactoryInterface = NILoggerFactory()

}
