package fr.rob.game.domain.setup.tasks

import fr.rob.core.initiator.TaskInterface
import fr.rob.game.domain.network.Server
import fr.rob.game.domain.setup.Setup
import fr.rob.game.domain.setup.tasks.repository.LoadServerRepositoryInterface

class TaskLoadServerConfig(
    private val servers: Array<Server>,
    private val repository: LoadServerRepositoryInterface,
    private val setup: Setup
) : TaskInterface {

    override fun run() {
        for (server in servers) {
            val serverInfo = repository.getServerInfo(server)

            server.zones = serverInfo.zones
            server.serverAddress = serverInfo.address

            if (server.zones.isEmpty()) {
                throw RuntimeException("Cannot find server name ${server.serverName} maps")
            }
        }

        setup.setServers(servers)
    }
}
