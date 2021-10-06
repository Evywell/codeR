package fr.rob.orchestrator.instances

import fr.rob.orchestrator.instances.Instance.Companion.TYPE_GLOBAL
import fr.rob.orchestrator.nodes.GameNode
import fr.rob.shared.orchestrator.Orchestrator

class InstanceManager(
    private val orchestrator: Orchestrator,
    private val createInstanceProcess: CreateInstanceProcess,
    private val defaultInstancesRepository: DefaultInstancesRepositoryInterface,
    private val instancesRepository: InstancesRepositoryInterface
) {

    val instances = ArrayList<Instance>()

    fun loadGlobalInstances() {
        instances.addAll(instancesRepository.getGlobalInstancesByOrchestratorAndType(orchestrator.id, TYPE_GLOBAL))
    }

    fun createInstancesForNode(node: GameNode): List<Instance> {
        val instancesForNode = ArrayList<Instance>()
        val defaultInstances = defaultInstancesRepository.getDefaultInstancesByNode(node)

        for (defaultInstance in defaultInstances) {
            var found = false

            for (instance in instances) {
                if (
                    defaultInstance.mapId == instance.mapId
                    && defaultInstance.zoneId == instance.zoneId
                ) {
                    found = true
                    instancesForNode.add(instance)

                    break
                }
            }

            if (!found) {
                val createdInstance = createInstanceProcess.create(
                    defaultInstance.mapId,
                    defaultInstance.zoneId,
                    orchestrator.id,
                    defaultInstance.type
                )

                instancesForNode.add(createdInstance)
                // It's a new instance, so we add it in the manager state
                instances.add(createdInstance)
            }
        }

        return instancesForNode
    }
}
