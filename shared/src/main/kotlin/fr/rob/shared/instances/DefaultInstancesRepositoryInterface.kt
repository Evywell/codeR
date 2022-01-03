package fr.rob.shared.instances

interface DefaultInstancesRepositoryInterface {

    fun getDefaultInstancesByNode(nodeName: String): List<DefaultInstance>
}
