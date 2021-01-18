package fr.rob.game.domain.network

import fr.rob.game.domain.world.World
import fr.rob.game.infrastructure.log.LoggerFactory

class GameServerManager (private val factory: GameServerFactoryInterface) {

    fun buildGameServers(servers: Array<Server>) {
        servers.forEach { server -> buildGameServer(server) }
    }

    private fun buildGameServer(server: Server) {
        val address: String = server.serverAddress!!
        val port: Int = parsePortFromAddress(address)

        val gs = factory.build(port, LoggerFactory.create(server.serverName!!))

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
