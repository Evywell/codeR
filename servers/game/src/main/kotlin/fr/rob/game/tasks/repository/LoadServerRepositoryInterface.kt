package fr.rob.game.tasks.repository

import fr.rob.game.network.Server
import fr.rob.game.network.Zone

interface LoadServerRepositoryInterface {

    fun getServerInfo(server: Server): ServerInfo
}

data class ServerInfo(
    var name: String,
    var address: String,
    var zones: List<Zone>
)
