package fr.rob.orchestrator.api.instance

interface DefaultInstancesRepositoryInterface {

    fun getDefaultInstancesByNode(nodeName: String): List<DefaultInstance>
}
