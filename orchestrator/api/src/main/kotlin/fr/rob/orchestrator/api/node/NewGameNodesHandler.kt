package fr.rob.orchestrator.api.node

import fr.raven.messaging.message.MessagingProto
import fr.raven.messaging.receive.MessageHandlerInterface
import fr.raven.messaging.send.MessageQueueDispatcherInterface
import fr.rob.orchestrator.api.instance.DefaultInstance
import fr.rob.orchestrator.api.instance.DefaultInstancesRepositoryInterface
import fr.rob.orchestrator.api.instance.Instance
import fr.rob.orchestrator.api.instance.InstanceManager
import fr.rob.orchestrator.shared.entities.CreateInstanceRequestProto
import fr.rob.orchestrator.shared.entities.NewGameNodeProto

class NewGameNodesHandler(
    private val nodeManager: NodeManager,
    private val defaultInstancesRepository: DefaultInstancesRepositoryInterface,
    private val instanceManager: InstanceManager,
    private val messageQueueDispatcher: MessageQueueDispatcherInterface
) : MessageHandlerInterface {

    override fun handle(envelope: MessagingProto.Envelope) {
        val message = NewGameNodeProto.NewGameNodes.parseFrom(envelope.data)

        for (node in message.nodesList) {
            val registeredNode = nodeManager.registerNode(node.name, node.port)
            val defaultInstances = defaultInstancesRepository.getDefaultInstancesByNode(node.name)
            val instancesToSend =
                instanceManager.createMultipleForNode(
                    registeredNode,
                    DefaultInstance.collectionIntoInstanceInfo(defaultInstances)
                )

            for (instance in instancesToSend) {
                messageQueueDispatcher.dispatch(
                    CreateInstanceRequestProto.CreateMapInstance.newBuilder()
                        .setMapId(instance.mapId)
                        .setZoneId(instance.zoneId)
                        .setNode(node.name)
                        .build()
                )

                instance.state = Instance.STATE_PENDING
            }
        }
    }
}
