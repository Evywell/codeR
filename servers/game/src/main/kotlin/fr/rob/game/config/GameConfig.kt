package fr.rob.game.config

import fr.rob.core.config.Config
import fr.rob.core.config.database.DatabaseConfig
import fr.rob.game.node.NodeConfig

data class GameConfig(
    val databases: Databases,
    val nodesConfig: ServerNodesConfig,
    val physic: PhysicConfig,
)

data class PhysicConfig(
    val host: String,
    val port: Int,
    val connectTimeoutMs: Int = DEFAULT_PHYSIC_CONNECT_TIMEOUT_MS,
    val retryDelayMs: Long = DEFAULT_PHYSIC_RETRY_DELAY_MS,
) {
    companion object {
        const val DEFAULT_PHYSIC_CONNECT_TIMEOUT_MS = 2_000
        const val DEFAULT_PHYSIC_RETRY_DELAY_MS = 1_000L
    }
}

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
