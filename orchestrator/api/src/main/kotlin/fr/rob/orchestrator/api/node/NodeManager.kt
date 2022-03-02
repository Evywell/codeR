package fr.rob.orchestrator.api.node

class NodeManager {

    private val nodes = ArrayList<Node>()

    fun registerNode(label: String, port: Int): Node {
        val info = NodeInfo(label, port)

        return Node(info)
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
