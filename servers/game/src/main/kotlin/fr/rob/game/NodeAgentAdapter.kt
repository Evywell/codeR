package fr.rob.game

import fr.rob.game.game.world.instance.MapInstance
import fr.rob.game.network.node.GameNodeManager
import fr.rob.orchestrator.agent.node.NodeAgentAdapterInterface

class NodeAgentAdapter(private val nodeManager: GameNodeManager) : NodeAgentAdapterInterface {
    override fun handleNewInstanceRequest(nodeName: String, mapId: Int, zoneId: Int?) {
        val node = nodeManager.getNode(nodeName) ?: return

        node.instanceManager.create(MapInstance(1, mapId, zoneId))
    }
}
