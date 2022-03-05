package fr.rob.web

import fr.raven.log.log4j.LoggerFactory
import fr.rob.client.network.Client
import fr.rob.core.process.ProcessManager
import fr.rob.game.opcode.ClientOpcodeHandler
import fr.rob.web.handler.OpcodeHandler
import fr.rob.web.network.HttpServer
import java.io.File

class Main {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val loggerFactory = LoggerFactory(File(Main::class.java.getResource("log4j.config.xml")!!.path))
            val logger = loggerFactory.create("web")
            val app = Application(logger)
            val processManager = ProcessManager()

            val client = Client(GAME_CLIENT_IP, GAME_CLIENT_PORT)
            client.open()

            val server = HttpServer(HTTP_SERVER_PORT)
            val opcodeHandler = ClientOpcodeHandler(processManager, logger)

            server
                .post("/opcode/{id}", OpcodeHandler(client, opcodeHandler), mapOf("id" to "[0-9]+"))
                .run()
        }
    }
}
