package fr.rob.game.game.world.map

import fr.rob.game.game.world.entity.template.Creature
import fr.rob.game.game.world.map.loader.WorldObjectsLoaderInterface

data class Map(val id: Int, val zoneId: Int, val mapInfo: MapInfo, val zoneInfo: ZoneInfo?) {

    lateinit var creatures: List<Creature>

    fun registerCreatures(loader: WorldObjectsLoaderInterface<Creature>) {
        creatures = loader.loadObjects(id)
    }
}
