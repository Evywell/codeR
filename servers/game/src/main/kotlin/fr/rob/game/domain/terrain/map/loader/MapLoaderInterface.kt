package fr.rob.game.domain.terrain.map.loader

import fr.rob.game.domain.terrain.map.Map

interface MapLoaderInterface {

    fun load(mapId: Int, zoneId: Int): Map
}
