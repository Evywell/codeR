package fr.rob.orchestrator.instances

import fr.rob.orchestrator.instances.Instance.Companion.TYPE_GLOBAL

open class CreateInstanceProcess(private val instancesRepository: InstancesRepositoryInterface) {

    fun create(mapId: Int, zoneId: Int?, orchestratorId: Int, type: Int = TYPE_GLOBAL): Instance =
        instancesRepository.insert(mapId, zoneId, type, orchestratorId)
}
