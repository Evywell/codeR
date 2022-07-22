package fr.rob.game.domain.terrain.map.loader

import fr.rob.game.domain.terrain.map.Map

class DatabaseMapLoader(private val mapRepository: MapRepositoryInterface) : MapLoaderInterface {

    override fun load(mapId: Int, zoneId: Int): Map {
        val (mapInfo, zoneInfo) = mapRepository.getMapInfo(mapId, zoneId)

        return Map(mapId, zoneId, mapInfo, zoneInfo)
    }
}
