package fr.rob.test.sandbox.tasks

import fr.rob.game.domain.network.Server
import fr.rob.game.domain.network.Zone
import fr.rob.game.domain.tasks.repository.LoadServerRepositoryInterface
import fr.rob.game.domain.tasks.repository.ServerInfo

class LoadServerRepository :
    LoadServerRepositoryInterface {

    override fun getServerInfo(server: Server): ServerInfo =
        ServerInfo(server.serverName!!, server.serverAddress!!, loadZonesForMapId(server.mapId!!))

    private fun loadZonesForMapId(mapId: Int): List<Zone> {
        if (mapId == 0) {
            return ArrayList()
        }

        return arrayListOf(
            Zone(mapId, 100, 100, 0, 0),
            Zone(mapId, 100, 100, 0, 0),
            Zone(mapId, 100, 100, 0, 0),
            Zone(mapId, 100, 100, 0, 0)
        )
    }
}
