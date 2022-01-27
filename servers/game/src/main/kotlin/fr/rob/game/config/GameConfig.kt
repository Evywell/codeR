package fr.rob.game.config

import fr.rob.core.config.Config
import fr.rob.core.config.database.DatabaseConfig
import fr.rob.game.DEFAULT_MAX_NODES
import fr.rob.game.network.node.GameNodeManager.NodeConfig

data class GameConfig(
    val orchestrator: Orchestrator,
    val databases: Databases,
    val nodesConfig: NodesConfig
)

data class Databases(val configDb: DatabaseConfig, val playersDb: DatabaseConfig, val worldDb: DatabaseConfig)

data class Orchestrator(
    val host: String,
    val port: Int
)

class NodesConfig(config: Config) {
    val maxNodes = config.getInteger("nodes.maxnodes") ?: DEFAULT_MAX_NODES
    val nodes: Array<NodeConfig>

    init {
        val labels = config.getStringArray("nodes.labels") ?: emptyArray()

        nodes = Array(labels.size) { i -> NodeConfig(
            labels[i],
            config.getInteger("nodes.${labels[i]}.port")!!
        ) }
    }
}
