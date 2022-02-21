package fr.rob.orchestrator.api.instance

interface InstancesRepositoryInterface {
    fun insert(mapId: Int, zoneId: Int, type: Int): Instance
    fun getGlobalInstancesByOrchestratorAndType(orchestratorId: Int, type: Int): List<Instance>
}
