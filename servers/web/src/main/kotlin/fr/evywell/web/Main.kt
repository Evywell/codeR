package fr.evywell.web

import fr.evywell.web.handler.HomePageHandler
import fr.evywell.web.handler.OpcodeHandler
import fr.evywell.web.network.HttpServer


class Main {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val server = HttpServer(1333)

            server
                .post("/opcode/{id}", OpcodeHandler(), mapOf("id" to "[0-9]+"))
                .get("/", HomePageHandler())
                .run()
        }
    }
}
