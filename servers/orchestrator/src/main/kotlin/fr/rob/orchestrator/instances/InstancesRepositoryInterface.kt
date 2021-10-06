package fr.rob.orchestrator.instances

interface InstancesRepositoryInterface {

    fun insert(mapId: Int, zoneId: Int?, type: Int, orchestratorId: Int): Instance
    fun insertZone(instanceId: Int, zoneId: Int)
    fun getGlobalInstancesByOrchestratorAndType(orchestratorId: Int, type: Int): List<Instance>
}
