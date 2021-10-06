package fr.rob.game.network

import fr.rob.core.network.ServerFactoryInterface
import fr.rob.game.game.world.World
import kotlin.concurrent.thread

class GameServerManager(private val factory: ServerFactoryInterface) {

    fun buildGameServers(servers: Array<Server>) {
        servers.forEach { server -> buildGameServer(server) }
    }

    private fun buildGameServer(server: Server) {
        val address: String = server.serverAddress!!
        val port: Int = parsePortFromAddress(address)

        println("Building server at address $address")
        val gs = factory.build(port, server.serverName!!)

        thread(start = true) {
            val world = World()

            world.initialize()
            world.loop()
        }

        println("Game server starting...")
        gs.start()
    }

    /**
     * Extracts the port form an address string such as IP:PORT
     * e.g. 0.0.0.0:1234 -> 1234
     */
    private fun parsePortFromAddress(address: String): Int {
        val splitAddress = address.split(':')

        return splitAddress.last().toInt()
    }
}
