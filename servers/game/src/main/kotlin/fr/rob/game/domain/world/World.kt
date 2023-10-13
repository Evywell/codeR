package fr.rob.game.domain.world

class World(private val updatableObjects: Array<UpdatableInterface>) {
    var updateState: WorldUpdateState = WorldUpdateState()
        private set

    fun update(deltaTime: Int) {
        updateState.timeElapsedSinceLastUpdate = deltaTime
        updateState.timeElapsedSinceStartup += deltaTime

        updatableObjects.forEach { it.update(deltaTime) }
    }
}
