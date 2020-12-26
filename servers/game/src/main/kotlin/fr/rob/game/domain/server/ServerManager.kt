package fr.rob.game.domain.server

abstract class ServerManager {

    fun buildGameServers(servers: Array<Server>) {
        servers.forEach { server -> buildGameServer(server) }
    }

    protected abstract fun buildGameServer(server: Server)
}