package fr.rob.game.map.loader

import fr.rob.game.map.Map

interface MapLoaderInterface {

    fun load(mapId: Int, zoneId: Int): Map
}
