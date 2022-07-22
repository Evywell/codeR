package fr.rob.game.domain.terrain.map.loader

interface WorldObjectsLoaderInterface<T> {

    fun loadObjects(mapId: Int): List<T>
}
