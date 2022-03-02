package fr.rob.orchestrator.api.node

import fr.raven.messaging.message.MessagingProto
import fr.raven.messaging.receive.MessageHandlerInterface
import fr.rob.orchestrator.api.instance.Instance
import fr.rob.orchestrator.api.instance.InstanceManager
import fr.rob.orchestrator.shared.entities.CreateInstanceRequestProto

class GameNodeCreatedHandler(private val nodeManager: NodeManager, private val instanceManager: InstanceManager) :
    MessageHandlerInterface {
    override fun handle(envelope: MessagingProto.Envelope) {
        val message = CreateInstanceRequestProto.MapInstanceCreated.parseFrom(envelope.data)

        val instanceContainer = instanceManager.getInstanceContainer(message.mapId, message.zoneId)
            ?: throw Exception("Cannot retrieve instance for mapId=${message.mapId} and zoneId=${message.zoneId}")

        val node = nodeManager.getNodeByName(message.node)

        if (instanceContainer.node == null || instanceContainer.node == node) {
            throw Exception("The retrieved instance seems to be wrong (not linked to the right node)")
        }

        instanceContainer.instance.state = Instance.STATE_DONE
    }
}
