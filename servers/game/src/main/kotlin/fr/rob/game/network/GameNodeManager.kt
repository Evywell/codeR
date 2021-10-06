package fr.rob.game.network

import fr.rob.core.network.ServerFactoryInterface
import fr.rob.game.config.server.NodesConfig

class GameNodeManager(
    private val nodesConfig: NodesConfig,
    private val serverFactory: ServerFactoryInterface
) {

    fun buildNodes() {
        for (nodeConfig in nodesConfig.nodes) {
            val server = serverFactory.build(nodeConfig.port, nodeConfig.label, false)

            // When started, the server triggers an event caught by the supervisor informing the orchestrator
            // that a new game node is available
            server.start()
        }

        if (nodesConfig.nodes.size >= nodesConfig.maxNodes) {
            return
        }

        // @todo loop over all remain server (nodesConfig.maxNodes - nodesConfig.nodes.size)
    }
}
