package fr.rob.game.game.world.map.loader

import fr.rob.game.game.world.map.MapInfo
import fr.rob.game.game.world.map.ZoneInfo

interface MapRepositoryInterface {

    fun getMapInfo(mapId: Int, zoneId: Int): Pair<MapInfo, ZoneInfo?>
}
