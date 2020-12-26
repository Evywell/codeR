package fr.rob.test

import fr.rob.game.domain.log.LoggerInterface
import fr.rob.test.sandbox.log.NILogger

open class BaseTest {

    val logger: LoggerInterface = NILogger()

}