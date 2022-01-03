package fr.rob.orchestrator.api.node

class NodeManager {

    private val nodes = ArrayList<Node>()

    fun registerNode(label: String, port: Int): Node {
        val info = NodeInfo(label, port)

        return Node(info)
    }
}
