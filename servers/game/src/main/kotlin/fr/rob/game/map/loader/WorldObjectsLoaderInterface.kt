package fr.rob.game.map.loader

interface WorldObjectsLoaderInterface<T> {

    fun loadObjects(mapId: Int): List<T>
}
