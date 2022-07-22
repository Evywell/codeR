package fr.rob.game.infra.config

import fr.raven.messaging.rabbitmq.AMQPConfig
import fr.rob.core.config.Config
import fr.rob.core.config.database.DatabaseConfig
import fr.rob.game.DEFAULT_MAX_NODES
import fr.rob.game.domain.node.NodeConfig

data class GameConfig(
    val orchestrator: Orchestrator,
    val databases: Databases,
    val nodesConfig: NodesConfig,
    val rabbitConfig: AMQPConfig,
)

data class Databases(val configDb: DatabaseConfig, val playersDb: DatabaseConfig, val worldDb: DatabaseConfig)

data class Orchestrator(
    val host: String,
    val port: Int
)

class NodesConfig(config: Config) {
    val maxNodes = config.getInteger("nodes.maxnodes") ?: DEFAULT_MAX_NODES
    val nodeConfig: NodeConfig

    init {
        nodeConfig = NodeConfig(
            config.getInteger("port")!!
        )
    }
}
