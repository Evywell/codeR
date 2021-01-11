package fr.rob.game.domain.world.map

import fr.rob.game.domain.world.map.Map
import fr.rob.game.domain.world.map.MapLoaderInterface

class FileMapLoader(var mapId: Int) : MapLoaderInterface {

    override fun load(): Map {
        // Read the file with the mapId
        // Create the map with the data

        return Map(mapId)
    }
}
