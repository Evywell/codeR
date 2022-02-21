package fr.rob.game

import fr.rob.core.messaging.send.MessageQueueDispatcherInterface
import fr.rob.game.config.GameConfig
import fr.rob.game.game.world.map.MapManager
import fr.rob.game.network.node.GameNodeManager
import fr.rob.orchestrator.agent.node.NodeAgentAdapterInterface
import fr.rob.orchestrator.shared.entities.NewGameNodeProto

class App(
    private val config: GameConfig,
    private val nodeManager: GameNodeManager,
    private val mapManager: MapManager,
    private val messageQueue: MessageQueueDispatcherInterface
) {

    fun run() {
        val builtNodes = nodeManager.buildNodes(config.nodesConfig.nodes)
        val presentationBuilder = NewGameNodeProto.NewGameNodes.newBuilder()

        for (builtNode in builtNodes) {
            presentationBuilder.addNodes(
                NewGameNodeProto.NewGameNode.newBuilder()
                    .setName(builtNode.info.name)
                    .setPort(builtNode.info.port)
            )
        }

        messageQueue.dispatch(presentationBuilder.build())
    }
}
