package fr.rob.game.game.world.map

import java.util.ArrayList

class MapManager(private val loader: MapLoaderInterface) {

    private val maps = ArrayList<Map>()

    fun createMap(): Map {
        val map: Map = loader.load()
        maps.add(map)

        return map
    }
}
