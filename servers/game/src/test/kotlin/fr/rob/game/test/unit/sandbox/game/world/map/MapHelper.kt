package fr.rob.game.test.unit.sandbox.game.world.map

import fr.rob.game.map.Map
import fr.rob.game.map.MapInfo
import fr.rob.game.map.ZoneInfo

class MapHelper {
    companion object {
        fun createDummyMap(): Map {
            val mapInfo = MapInfo("Dummy map info", 100, 100)
            val zoneInfo = ZoneInfo("Dummy zone info", 100, 100, 0f, 0f)

            return Map(0, 0, mapInfo, zoneInfo)
        }
    }
}
