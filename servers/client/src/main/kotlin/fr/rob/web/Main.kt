package fr.rob.web

import fr.rob.client.network.Client
import fr.rob.game.domain.opcode.ClientOpcodeHandler
import fr.rob.game.infrastructure.log.LoggerFactory
import fr.rob.web.handler.HomePageHandler
import fr.rob.web.handler.OpcodeHandler
import fr.rob.web.network.HttpServer


class Main {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val client = Client("localhost", 8889)
            client.start()

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
