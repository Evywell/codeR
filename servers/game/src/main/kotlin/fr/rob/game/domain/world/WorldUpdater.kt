package fr.rob.game.domain.world

import fr.rob.core.misc.clock.IntervalTimer

class WorldUpdater(
    private val updatableObjects: Array<UpdatableInterface>
) {
    private var worldUps = 0
    private val worldRateTimer = IntervalTimer(1000)

    fun update(deltaTime: Int) {
        checkWorldUpdateRate(deltaTime)
        updatableObjects.forEach { it.update(deltaTime) }
    }

    private fun checkWorldUpdateRate(deltaTime: Int) {
        worldRateTimer.update(deltaTime)
        worldUps += 1

        if (worldRateTimer.passed()) {
            if (worldUps <= WORLD_UPDATE_THRESHOLD_ERROR) {
                println("[ERROR] World update rate too low: $worldUps")
            }

            worldUps = 0
            worldRateTimer.reset()
        }
    }

    companion object {
        private const val WORLD_UPDATE_THRESHOLD_ERROR = World.WORLD_UPDATE_PER_SECOND - 2
    }
}
