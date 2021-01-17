package fr.rob.test

import fr.rob.game.domain.log.LoggerFactoryInterface
import fr.rob.game.domain.log.LoggerInterface
import fr.rob.game.domain.network.GameServer
import fr.rob.game.domain.process.ProcessManager
import fr.rob.test.sandbox.log.NILogger
import fr.rob.test.sandbox.log.NILoggerFactory
import java.net.URL

open class BaseTest {

    val logger: LoggerInterface = NILogger()
    val loggerFactory: LoggerFactoryInterface = NILoggerFactory()

    fun getResourceURL(resourcePath: String): URL? = BaseTest::class.java.classLoader.getResource(resourcePath)

    fun getGameServer(): GameServer = GameServer("test", loggerFactory, ProcessManager())
}
