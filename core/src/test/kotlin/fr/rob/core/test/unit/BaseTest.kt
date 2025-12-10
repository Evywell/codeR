package fr.rob.core.test.unit

import fr.raven.log.LoggerFactoryInterface
import fr.raven.log.LoggerInterface
import fr.rob.core.event.EventManagerInterface
import fr.rob.core.network.Packet
import fr.rob.core.network.v2.Server
import fr.rob.core.opcode.OpcodeHandler
import fr.rob.core.test.unit.sandbox.event.NIEventManager
import fr.rob.core.test.unit.sandbox.log.NILogger
import fr.rob.core.test.unit.sandbox.log.NILoggerFactory
import fr.rob.core.test.unit.sandbox.network.NIServer
import fr.rob.core.test.unit.sandbox.opcode.NIOpcodeHandler
import java.net.URL

open class BaseTest {

    val logger: LoggerInterface = NILogger()
    val loggerFactory: LoggerFactoryInterface = NILoggerFactory()
    val opcodeHandler: OpcodeHandler = NIOpcodeHandler(logger)
    var server: Server<Packet> = NIServer()
    val eventManager: EventManagerInterface = NIEventManager()

    fun getResourceURL(resourcePath: String): URL? = BaseTest::class.java.classLoader.getResource(resourcePath)

    fun getGameServer(): Server<Packet> = NIServer()
}
