package fr.rob.game.world

import fr.rob.core.misc.clock.IntervalTimer

class WorldUpdateRateChecker : UpdatableInterface {
    private var worldUps = 0
    private val worldRateTimer = IntervalTimer(1000)

    override fun update(deltaTime: Int) {
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
        private const val WORLD_UPDATE_THRESHOLD_ERROR = WorldUpdater.WORLD_UPDATE_PER_SECOND - 2
    }
}
