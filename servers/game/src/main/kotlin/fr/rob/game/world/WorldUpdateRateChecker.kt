package fr.rob.game.world

import fr.raven.log.LoggerInterface
import fr.rob.core.misc.clock.IntervalTimer

class WorldUpdateRateChecker(private val logger: LoggerInterface) : UpdatableInterface {
    private var worldUps = 0
    private val worldRateTimer = IntervalTimer(1000)

    override fun update(deltaTime: Int) {
        worldRateTimer.update(deltaTime)
        worldUps += 1

        if (worldRateTimer.passed()) {
            if (worldUps <= WORLD_UPDATE_THRESHOLD_ERROR) {
                logger.error("World update rate too low: {} ups (target {})", worldUps, WorldUpdater.WORLD_UPDATE_PER_SECOND)
            }

            worldUps = 0
            worldRateTimer.reset()
        }
    }

    companion object {
        private const val WORLD_UPDATE_THRESHOLD_ERROR = WorldUpdater.WORLD_UPDATE_PER_SECOND - 2
    }
}
