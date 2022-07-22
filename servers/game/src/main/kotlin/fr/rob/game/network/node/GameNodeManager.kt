package fr.rob.game.network.node

import fr.rob.game.app.node.GameNode
import fr.rob.game.domain.instance.InstanceManager

class GameNodeManager(
    private val maxNodes: Int,
    private val instanceManager: InstanceManager,
) {

    private val nodes = ArrayList<GameNode>()

    fun getNode(name: String): GameNode? {
        for (node in nodes) {
            if (name == node.info.name) {
                return node
            }
        }

        return null
    }
}
