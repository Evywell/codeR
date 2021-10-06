package fr.rob.game.config.server

import fr.rob.core.config.Config
import fr.rob.core.config.ConfigHandlerInterface
import fr.rob.game.SERVER
import fr.rob.game.network.Server

class ServerConfigHandler : ConfigHandlerInterface {

    override fun getConfigKey(): String = SERVER

    override fun handle(config: Config): Any? {
        val serverNames = config.getStringArray(SERVER)

        if (serverNames === null) {
            return null
        }

        return Array(serverNames.size) { i: Int -> Server(serverName = serverNames[i]) }
    }
}
