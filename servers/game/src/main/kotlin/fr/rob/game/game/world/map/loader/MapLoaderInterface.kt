package fr.rob.game.game.world.map.loader

import fr.rob.game.game.world.map.Map

interface MapLoaderInterface {

    fun load(mapId: Int, zoneId: Int): Map
}
