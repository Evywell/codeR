package fr.rob.orchestrator.nodes

import fr.rob.entities.orchestrator.NewGameNodeProto
import fr.rob.orchestrator.network.OrchestratorSession

class GameNodeManager {

    private val nodes = ArrayList<GameNode>()

    fun register(
        ownerIp: String,
        newGameNode: NewGameNodeProto.NewGameNode,
        ownerAgent: OrchestratorSession
    ): GameNode {
        val node = GameNode(ownerIp, newGameNode.port, newGameNode.name, ownerAgent)
        nodes.add(node)

        return node
    }
}
