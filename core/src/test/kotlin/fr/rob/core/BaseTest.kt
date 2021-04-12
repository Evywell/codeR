package fr.rob.core

import fr.rob.core.log.LoggerFactoryInterface
import fr.rob.core.log.LoggerInterface
import fr.rob.core.network.Server
import fr.rob.core.opcode.OpcodeHandler
import fr.rob.core.process.ProcessManager
import fr.rob.core.sandbox.NIApplication
import fr.rob.core.sandbox.log.NILogger
import fr.rob.core.sandbox.log.NILoggerFactory
import fr.rob.core.sandbox.opcode.NIOpcodeHandler
import java.net.URL

open class BaseTest {

    val logger: LoggerInterface = NILogger()
    val loggerFactory: LoggerFactoryInterface = NILoggerFactory()
    val app: BaseApplication = NIApplication()
    val opcodeHandler: OpcodeHandler = NIOpcodeHandler(logger)
    val processManager: ProcessManager = ProcessManager()

    fun getResourceURL(resourcePath: String): URL? = BaseTest::class.java.classLoader.getResource(resourcePath)

    fun getGameServer(): Server = Server()
}