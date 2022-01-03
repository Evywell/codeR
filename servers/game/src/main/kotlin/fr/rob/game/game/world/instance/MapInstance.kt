package fr.rob.game.game.world.instance

/**
 * We use MapInstance instead of Instance to avoid confusion with the entity
 */
data class MapInstance(val id: Int, val mapId: Int, val zoneId: Int?) {

    fun update(deltaTime: Int) {}
}
