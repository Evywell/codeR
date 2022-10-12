package fr.rob.game.domain.world

class WorldUpdater(
    private val updatableObjects: Array<UpdatableInterface>
) {
    fun update(deltaTime: Long) {
        updatableObjects.forEach { it.update(deltaTime) }
    }
}
