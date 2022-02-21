package fr.rob.orchestrator.api.instance

data class DefaultInstance(val mapId: Int, val zoneId: Int, val nodeName: String, val type: Int) {

    companion object {
        fun collectionIntoInstanceInfo(defaultInstances: List<DefaultInstance>): List<InstanceManager.InstanceInfo> {
            val instanceInfo = ArrayList<InstanceManager.InstanceInfo>()

            for (instance in defaultInstances) {
                instanceInfo.add(InstanceManager.InstanceInfo(instance.mapId, instance.zoneId, instance.type))
            }

            return instanceInfo
        }
    }
}
