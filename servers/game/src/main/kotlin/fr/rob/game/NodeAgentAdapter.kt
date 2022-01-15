package fr.rob.game

import fr.rob.game.game.world.map.MapManager
import fr.rob.game.network.node.GameNodeManager
import fr.rob.orchestrator.agent.node.NodeAgentAdapterInterface

class NodeAgentAdapter(private val nodeManager: GameNodeManager, private val mapManager: MapManager) :
    NodeAgentAdapterInterface {
    override fun handleNewInstanceRequest(nodeName: String, mapId: Int, zoneId: Int?) {
        val node = nodeManager.getNode(nodeName) ?: return
        val map = mapManager.getMap(mapId, zoneId)

        node.instanceManager.create(1, map) // @todo change by a method parameter
    }
}
