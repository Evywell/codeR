package fr.rob.game.infra.network.messaging

import fr.raven.messaging.message.MessagingProto
import fr.raven.messaging.receive.MessageHandlerInterface
import fr.raven.messaging.send.MessageQueueDispatcher
import fr.rob.game.app.node.GameNode
import fr.rob.game.domain.instance.MapInstance
import fr.rob.game.domain.terrain.map.MapManager
import fr.rob.game.network.node.GameNodeManager
import fr.rob.orchestrator.shared.entities.CreateInstanceRequestProto

class CreateInstanceHandler(
    private val nodeManager: GameNodeManager,
    private val mapManager: MapManager,
    private val messageQueueDispatcher: MessageQueueDispatcher,
) : MessageHandlerInterface {
    override fun handle(envelope: MessagingProto.Envelope) {
        val message = CreateInstanceRequestProto.CreateMapInstance.parseFrom(envelope.data)

        val node = nodeManager.getNode(message.node) ?: return
        val map = mapManager.getMap(message.mapId, message.zoneId)

        val createdInstance = node.instanceManager.create(1, map) // @todo change by a method parameter

        notifyInstanceCreated(createdInstance, node)
    }

    private fun notifyInstanceCreated(instance: MapInstance, node: GameNode) {
        messageQueueDispatcher.dispatch(
            CreateInstanceRequestProto.MapInstanceCreated.newBuilder()
                .setMapId(instance.map.id)
                .setZoneId(instance.map.zoneId)
                .setNode(node.info.name)
                .build(),
        )
    }
}
