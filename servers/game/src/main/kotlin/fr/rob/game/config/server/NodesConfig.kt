package fr.rob.game.config.server

import fr.rob.core.config.Config
import fr.rob.core.config.ConfigHandlerInterface
import fr.rob.game.DEFAULT_MAX_NODES

class NodesConfigHandler : ConfigHandlerInterface {
    override fun getConfigKey(): String = "nodes"

    override fun handle(config: Config): Any? {
        val maxNodes = config.getInteger("nodes.maxnodes", DEFAULT_MAX_NODES)
        val labels = config.getStringArray("nodes.labels") ?: emptyArray()

        val nodes = ArrayList<NodeConfig>()

        for (label in labels) {
            nodes.add(NodeConfig(label, getNodeConfigPort(config, label)))
        }

        return NodesConfig(maxNodes!!, nodes)
    }

    private fun getNodeConfigPort(config: Config, nodeLabel: String): Int =
        config.getInteger("nodes.$nodeLabel.port")!!
}

data class NodesConfig(val maxNodes: Int, val nodes: List<NodeConfig>)

data class NodeConfig(val label: String, val port: Int)
