package fr.rob.orchestrator.api.composer

import fr.rob.orchestrator.api.instance.Instance
import fr.rob.orchestrator.api.node.Node
import fr.rob.orchestrator.shared.entities.CreateInstanceRequestProto

data class CreateInstanceRequest(val requestId: String, val instance: Instance, val node: Node) {

    fun buildProto(): CreateInstanceRequestProto.CreateMapInstanceRequest =
        CreateInstanceRequestProto.CreateMapInstanceRequest.newBuilder()
            .setRequestId(requestId)
            .setMapId(instance.mapId)
            .setZoneId(instance.zoneId ?: 0)
            .setNode(node.info.label)
            .build()
}
