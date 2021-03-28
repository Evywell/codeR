package fr.rob.game.domain.network

import fr.rob.core.network.ServerFactoryInterface
import fr.rob.game.domain.game.world.World

class GameServerManager(private val factory: ServerFactoryInterface) {

    fun buildGameServers(servers: Array<Server>) {
        servers.forEach { server -> buildGameServer(server) }
    }

    private fun buildGameServer(server: Server) {
        val address: String = server.serverAddress!!
        val port: Int = parsePortFromAddress(address)

        println("build server $address")
        val gs = factory.build(port, server.serverName!!)

        val world = World()

        /*
        world.initialize()
        world.loop()
         */

        // @todo: Initiate the world and MapManager using server.mapId
        // @todo: Make the world loop and launch it, then start the server after all is successfully initialized

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
