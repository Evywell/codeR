package fr.rob.game.instance

import fr.rob.game.instance.InstanceManager
import fr.rob.game.map.MapManager

class FakeInstanceBuilder(
    private val mapManager: MapManager,
    private val instanceManager: InstanceManager
) {
    fun buildInstance(instanceId: Int, mapId: Int, zoneId: Int) {
        val map = mapManager.getMap(mapId, zoneId)

        instanceManager.create(instanceId, map)
    }
}
