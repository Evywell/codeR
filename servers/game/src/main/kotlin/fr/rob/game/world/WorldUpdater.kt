package fr.rob.game.world

import fr.raven.log.LoggerInterface

class WorldUpdater(
    private val world: World,
    private val updatableObjects: Array<UpdatableInterface>,
    private val logger: LoggerInterface,
) {

    private var isRunning: Boolean = false
    var deltaTime: Int = 0
        private set

    fun initialize() {
        isRunning = true
    }

    fun loop() {
        var realCurrentTime: Long
        var realPreviousTime = System.currentTimeMillis()
        var nextTickAt = realPreviousTime

        while (isRunning) {
            realCurrentTime = System.currentTimeMillis()

            deltaTime = (realCurrentTime - realPreviousTime).toInt()

            world.update(deltaTime)
            updatableObjects.forEach { it.update(deltaTime) }

            realPreviousTime = realCurrentTime

            nextTickAt += WORLD_UPDATE_INTERVAL
            val remaining = nextTickAt - System.currentTimeMillis()

            if (remaining > 0) {
                sleepPrecisely(remaining)
            } else if (-remaining > WORLD_UPDATE_INTERVAL) {
                // We've drifted behind by more than a full tick (e.g. long GC pause, system suspend).
                // Resynchronize the schedule instead of endlessly trying to catch up in a burst.
                logger.warning(
                    "Tick loop fell behind by {}ms (budget {}ms); resynchronizing schedule",
                    -remaining,
                    WORLD_UPDATE_INTERVAL,
                )
                nextTickAt = System.currentTimeMillis()
            }
        }
    }

    /**
     * Thread.sleep() alone tends to overshoot its requested duration due to OS scheduler/timer
     * granularity (this varies significantly between platforms, e.g. Linux vs macOS/Windows).
     * To keep the tick rate accurate regardless of platform, sleep for most of the duration and
     * busy-spin the last couple of milliseconds for precision.
     */
    private fun sleepPrecisely(durationMs: Long) {
        val spinMarginMs = minOf(SPIN_MARGIN_MS, durationMs)
        val sleepMs = durationMs - spinMarginMs

        if (sleepMs > 0) {
            Thread.sleep(sleepMs)
        }

        val deadline = System.nanoTime() + spinMarginMs * NANOS_PER_MS
        while (System.nanoTime() < deadline) {
            Thread.onSpinWait()
        }
    }

    companion object {
        const val WORLD_UPDATE_PER_SECOND = 50
        const val WORLD_UPDATE_INTERVAL = 1000 / WORLD_UPDATE_PER_SECOND
        private const val SPIN_MARGIN_MS = 2L
        private const val NANOS_PER_MS = 1_000_000L
    }
}
