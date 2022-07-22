package fr.rob.orchestrator.api.node

import fr.raven.messaging.message.MessagingProto
import fr.raven.messaging.receive.MessageHandlerInterface
import fr.rob.orchestrator.shared.entities.NewGameNodeProto

class NewGameNodesHandler(
    private val nodeManager: NodeManager,
    private val nodeBuilder: NodeBuilder,
) : MessageHandlerInterface {

    override fun handle(envelope: MessagingProto.Envelope) {
        val message = NewGameNodeProto.NewGameNode.parseFrom(envelope.data)

        val node = nodeBuilder.buildFromPort(message.port)
        nodeManager.registerNode(node)
    }
}
