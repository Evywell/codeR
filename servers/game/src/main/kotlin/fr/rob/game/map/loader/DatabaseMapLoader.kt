package fr.rob.game.map.loader

import fr.rob.game.map.Map
import fr.rob.game.persistence.map.MapRepositoryInterface

class DatabaseMapLoader(private val mapRepository: MapRepositoryInterface) : MapLoaderInterface {

    override fun load(mapId: Int, zoneId: Int): Map {
        val (mapInfo, zoneInfo) = mapRepository.getMapInfo(mapId, zoneId)

        return Map(mapId, zoneId, mapInfo, zoneInfo)
    }
}
