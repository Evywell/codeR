package fr.rob.game.domain.tasks.repository

import fr.rob.game.domain.network.Server
import fr.rob.game.domain.network.Zone

interface LoadServerRepositoryInterface {

    fun getServerInfo(server: Server): ServerInfo
}

data class ServerInfo(
    var name: String,
    var address: String,
    var zones: List<Zone>
)
