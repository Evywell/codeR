package fr.rob.game.domain.terrain.map

import fr.rob.game.domain.entity.template.Creature
import fr.rob.game.domain.terrain.map.loader.MapLoaderInterface
import fr.rob.game.domain.terrain.map.loader.WorldObjectsLoaderInterface

class MapManager(
    private val loader: MapLoaderInterface,
    private val creatureLoader: WorldObjectsLoaderInterface<Creature>
) {

    private val maps = ArrayList<Map>()

    fun getMap(mapId: Int, zoneId: Int): Map {
        return retrieveFromInfo(mapId, zoneId) ?: createMap(mapId, zoneId)
    }

    private fun createMap(mapId: Int, zoneId: Int): Map {
        val map: Map = loader.load(mapId, zoneId)

        map.registerCreatures(creatureLoader)

        maps.add(map)

        return map
    }

    private fun retrieveFromInfo(mapId: Int, zoneId: Int): Map? {
        for (map in maps) {
            if (map.id == mapId && map.zoneId == zoneId) {
                return map
            }
        }

        return null
    }
}
