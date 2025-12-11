package fr.rob.game.map

import fr.rob.game.entity.template.Creature
import fr.rob.game.map.loader.WorldObjectsLoaderInterface

data class Map(val id: Int, val zoneId: Int, val mapInfo: MapInfo, val zoneInfo: ZoneInfo) {

    lateinit var creatures: List<Creature>

    fun registerCreatures(loader: WorldObjectsLoaderInterface<Creature>) {
        creatures = loader.loadObjects(id)
    }
}
