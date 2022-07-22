package fr.rob.game.domain.terrain.map.loader

import fr.rob.game.domain.terrain.map.MapInfo
import fr.rob.game.domain.terrain.map.ZoneInfo

interface MapRepositoryInterface {

    fun getMapInfo(mapId: Int, zoneId: Int): Pair<MapInfo, ZoneInfo>
}
