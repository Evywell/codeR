package fr.rob.gateway.extension.realm.gamenode

import fr.rob.gateway.extension.game.GameNode
import java.util.Collections
import java.util.Optional

class GameNodes {
    private val nodes = Collections.synchronizedList(ArrayList<GameNode>())

    fun addNode(node: GameNode) {
        nodes.add(node)
    }

    fun removeGameNode(gameNode: GameNode) {
        nodes.remove(gameNode)
    }

    fun findByLabel(nodeLabel: String): GameNode? =
        nodes.find { it.label == nodeLabel }
}
