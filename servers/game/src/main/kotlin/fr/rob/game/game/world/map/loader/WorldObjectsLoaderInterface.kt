package fr.rob.game.game.world.map.loader

interface WorldObjectsLoaderInterface<T> {

    fun loadObjects(mapId: Int): List<T>
}
