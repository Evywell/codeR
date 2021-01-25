package fr.rob.web

import fr.rob.client.network.Client
import fr.rob.web.handler.HomePageHandler
import fr.rob.web.handler.OpcodeHandler
import fr.rob.web.network.HttpServer
import fr.rob.game.domain.opcode.ClientOpcodeHandler
import fr.rob.game.infrastructure.log.LoggerFactory

class Main {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val client = Client("127.0.0.1", 8889)
            client.open()

            val server = HttpServer(1333)
            val logger = LoggerFactory.create("web")
            val opcodeHandler = ClientOpcodeHandler(logger)

            server
                .post("/opcode/{id}", OpcodeHandler(client, opcodeHandler), mapOf("id" to "[0-9]+"))
                .get("/", HomePageHandler())
                .run()
        }
    }
}
