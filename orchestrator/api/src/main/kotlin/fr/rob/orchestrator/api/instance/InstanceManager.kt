package fr.rob.orchestrator.api.instance

import fr.rob.orchestrator.api.node.Node

class InstanceManager(private val instancesRepository: InstancesRepositoryInterface) {

    private val instances = ArrayList<InstanceContainer>()

    fun getInstanceContainer(mapId: Int, zoneId: Int): InstanceContainer? {
        for (container in instances) {
            if (
                container.instance.mapId == mapId
                && container.instance.zoneId == zoneId
            ) {
                return container
            }
        }

        return null
    }

    fun createMultipleForNode(node: Node, instanceInfoList: List<InstanceInfo>): List<Instance> {
        // The goal here is to create instances for node when needed, so we need to find if an instance already exists
        // In order to match the exact instance, we need to test the mapId and the zoneId at the same time

        val instancesToSend = ArrayList<Instance>()

        for (instanceInfo in instanceInfoList) {
            var fullMatch = false

            for (instanceContainer in instances) {
                if (
                    instanceInfo.mapId == instanceContainer.instance.mapId
                    && instanceInfo.zoneId == instanceContainer.instance.zoneId
                ) {
                    // It's a full match, we already have this instance
                    fullMatch = true

                    // Is the instance linked to a node already ?
                    if (instanceContainer.node == null) {
                        instanceContainer.node = node
                        instancesToSend.add(instanceContainer.instance)
                    }

                    break
                }
            }

            if (!fullMatch) {
                // We need to create the instance
                val instance = instancesRepository.insert(instanceInfo.mapId, instanceInfo.zoneId, instanceInfo.type)

                instances.add(InstanceContainer(instance, node))
                instancesToSend.add(instance)
            }
        }

        return instancesToSend
    }

    data class InstanceInfo(val mapId: Int, val zoneId: Int, val type: Int)
}
