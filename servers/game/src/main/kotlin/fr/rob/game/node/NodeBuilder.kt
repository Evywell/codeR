package fr.rob.game.node

class NodeBuilder {
    fun buildFromConfig(nodeConfig: NodeConfig): Node = Node(nodeConfig.port)
}
