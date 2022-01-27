package fr.rob.game

import fr.rob.game.config.GameConfig
import fr.rob.game.game.world.map.MapManager
import fr.rob.game.network.node.GameNodeManager
import fr.rob.orchestrator.agent.node.NodeAgentAdapterInterface

class App(
    private val config: GameConfig,
    private val nodeManager: GameNodeManager,
    private val mapManager: MapManager,
    private val nodeAgentAdapter: NodeAgentAdapterInterface
) {

    fun run() {
        val buildNodes = nodeManager.buildNodes(config.nodesConfig.nodes)
    }
}
