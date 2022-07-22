package fr.rob.game.app.node

import fr.rob.game.domain.instance.InstanceManager
import fr.rob.game.domain.instance.MapInstance
import fr.rob.game.infra.network.server.GameNodeServer

class GameNode(val info: GameNodeInfo, private val server: GameNodeServer, val instanceManager: InstanceManager) {

    fun retrieveInstanceWithInfo(instanceId: Int, mapId: Int, zoneId: Int): MapInstance? {
        for (instance in instanceManager.getAllInstances()) {
            if (
                instance.id == instanceId
                && instance.map.id == mapId
                && instance.map.zoneId == zoneId
            ) {
                return instance
            }
        }

        return null
    }
}
