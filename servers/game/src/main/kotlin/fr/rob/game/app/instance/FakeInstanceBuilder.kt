package fr.rob.game.app.instance

import fr.rob.game.domain.instance.InstanceManager
import fr.rob.game.domain.terrain.map.MapManager

class FakeInstanceBuilder(
    private val mapManager: MapManager,
    private val instanceManager: InstanceManager
) {
    fun buildInstance(instanceId: Int, mapId: Int, zoneId: Int) {
        val map = mapManager.getMap(mapId, zoneId)

        instanceManager.create(instanceId, map)
    }
}
