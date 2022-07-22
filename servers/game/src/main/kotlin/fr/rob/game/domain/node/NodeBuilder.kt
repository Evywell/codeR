package fr.rob.game.domain.node

class NodeBuilder {
    fun buildFromConfig(nodeConfig: NodeConfig): Node = Node(nodeConfig.port)
}
