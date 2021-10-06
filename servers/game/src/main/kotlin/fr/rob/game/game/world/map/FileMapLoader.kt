package fr.rob.game.game.world.map

class FileMapLoader(var mapId: Int) : MapLoaderInterface {

    override fun load(): Map {
        // Read the file with the mapId
        // Create the map with the data

        return Map(mapId)
    }
}
