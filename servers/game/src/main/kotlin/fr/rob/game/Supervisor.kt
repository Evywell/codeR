package fr.rob.game

import fr.rob.core.messaging.send.MessageQueueDispatcherInterface
import fr.rob.game.network.node.GameNodeManager
import fr.rob.orchestrator.shared.entities.NewGameNodeProto

class Supervisor(
    private val nodeManager: GameNodeManager,
    private val messageQueueDispatcher: MessageQueueDispatcherInterface,
    private val nodes: Array<GameNodeManager.NodeConfig>
) {

    fun run() {
        val builtNodes = nodeManager.buildNodes(nodes)
        val presentationBuilder = NewGameNodeProto.NewGameNodes.newBuilder()

        for (builtNode in builtNodes) {
            presentationBuilder.addNodes(
                NewGameNodeProto.NewGameNode.newBuilder()
                    .setName(builtNode.info.name)
                    .setPort(builtNode.info.port)
            )
        }

        messageQueueDispatcher.dispatch(presentationBuilder.build())
    }
}
