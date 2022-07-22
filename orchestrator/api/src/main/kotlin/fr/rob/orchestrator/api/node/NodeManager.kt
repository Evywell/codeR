package fr.rob.orchestrator.api.node

class NodeManager {

    private val nodes = ArrayList<Node>()

    fun registerNode(node: Node) {
        nodes.add(node)
    }

    fun getNodeByName(name: String): Node? {
        for (node in nodes) {
            if (node.info.label == name) {
                return node
            }
        }

        return null
    }
}
