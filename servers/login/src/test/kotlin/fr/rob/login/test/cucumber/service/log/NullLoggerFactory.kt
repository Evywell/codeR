package fr.rob.login.test.cucumber.service.log

import fr.raven.log.LoggerFactoryInterface
import fr.raven.log.LoggerInterface
import fr.raven.log.NullLogger

class NullLoggerFactory : LoggerFactoryInterface {
    override fun create(name: String): LoggerInterface = NullLogger()
}
