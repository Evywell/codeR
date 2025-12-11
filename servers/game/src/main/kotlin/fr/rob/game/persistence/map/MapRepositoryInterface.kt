package fr.rob.game.persistence.map

import fr.rob.game.map.MapInfo
import fr.rob.game.map.ZoneInfo

interface MapRepositoryInterface {

    fun getMapInfo(mapId: Int, zoneId: Int): Pair<MapInfo, ZoneInfo>
}
