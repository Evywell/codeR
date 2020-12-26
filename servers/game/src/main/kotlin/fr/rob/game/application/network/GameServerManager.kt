package fr.rob.game.application.network

import fr.rob.game.application.log.LoggerFactory
import fr.rob.game.domain.server.Server
import fr.rob.game.domain.server.ServerManager

class GameServerManager : ServerManager() {

    override fun buildGameServer(server: Server) {
        val address: String = server.serverAddress!!
        val port: Int = parsePortFromAddress(address)
        val gs = GameServer(port, LoggerFactory.create(server.serverName!!))

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