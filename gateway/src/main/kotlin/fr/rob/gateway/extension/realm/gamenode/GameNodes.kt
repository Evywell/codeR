package fr.rob.gateway.extension.realm.gamenode

import fr.rob.gateway.extension.game.GameNode

class GameNodes {
    private val nodes = ArrayList<GameNode>()

    fun addNode(node: GameNode) {
        nodes.add(node)
    }

    fun getNodes(): List<GameNode> = nodes
}
