package fr.rob.orchestrator.instances.request

import fr.rob.entities.orchestrator.CreateMapInstanceRequestProto
import fr.rob.orchestrator.instances.Instance

class RequestNewInstanceProcess(
    private val instanceRequestGenerator: InstanceRequestGeneratorInterface
) {

    fun createRequest(instance: Instance): CreateMapInstanceRequestProto.CreateMapInstanceRequest {
        val requestId = instanceRequestGenerator.generate()

        val request = CreateMapInstanceRequestProto.CreateMapInstanceRequest.newBuilder().apply {
            this.requestId = requestId
            this.instanceId = instance.id
            this.mapId = instance.mapId
            instance.zoneId?.let { this.zoneId = it }
        }

        return request.build()
    }
}
