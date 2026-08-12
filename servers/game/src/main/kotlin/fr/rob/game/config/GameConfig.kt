package fr.rob.game.config

import fr.rob.core.config.Config
import fr.rob.core.config.database.DatabaseConfig
import fr.rob.game.node.NodeConfig

data class GameConfig(
    val databases: Databases,
    val nodesConfig: ServerNodesConfig,
)

data class Databases(val configDb: DatabaseConfig, val playersDb: DatabaseConfig, val worldDb: DatabaseConfig)

class ServerNodesConfig(config: Config) {
    val maxNodes = config.getInteger("nodes.maxnodes") ?: DEFAULT_MAX_NODES
    val nodeConfig: NodeConfig

    init {
        nodeConfig = NodeConfig(
            config.getInteger("port")!!
        )
    }
}
