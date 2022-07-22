package fr.rob.orchestrator.api.node

import java.util.UUID

class NodeBuilder {
    fun buildFromPort(port: Int): Node {
        val info = NodeInfo(UUID.randomUUID().toString(), port)

        return Node(info)
    }
}
