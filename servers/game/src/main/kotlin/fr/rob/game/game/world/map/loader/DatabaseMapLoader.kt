package fr.rob.game.game.world.map.loader

import fr.rob.game.game.world.map.Map

class DatabaseMapLoader(private val mapRepository: MapRepositoryInterface) : MapLoaderInterface {

    override fun load(mapId: Int, zoneId: Int): Map {
        val (mapInfo, zoneInfo) = mapRepository.getMapInfo(mapId, zoneId)

        return Map(mapId, zoneId, mapInfo, zoneInfo)
    }
}
