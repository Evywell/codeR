package fr.rob.web

import fr.rob.client.network.Client
import fr.rob.web.handler.OpcodeHandler
import fr.rob.web.network.HttpServer
import fr.rob.game.domain.opcode.ClientOpcodeHandler
import fr.rob.game.domain.process.ProcessManager
import fr.rob.game.infrastructure.log.LoggerFactory

class Main {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val app = Application()
            val processManager = ProcessManager()

            val client = Client(GAME_CLIENT_IP, GAME_CLIENT_PORT)
            client.open()

            val server = HttpServer(HTTP_SERVER_PORT)
            val logger = LoggerFactory.create("web")
            val opcodeHandler = ClientOpcodeHandler(processManager, app, logger)

            server
                .post("/opcode/{id}", OpcodeHandler(client, opcodeHandler), mapOf("id" to "[0-9]+"))
                .run()
        }
    }
}
