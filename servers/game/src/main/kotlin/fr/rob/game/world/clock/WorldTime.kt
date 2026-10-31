package fr.rob.game.world.clock

/**
 * Time of the current world tick. Single instance, advanced by the world updater at the start of each tick.
 * Inject it in the services that need time instead of reading the system clock.
 *
 * Only read it from the world thread.
 */
class WorldTime {
    /**
     * Simulation time since the previous tick, clamped to [0, MAX_DELTA_MS]. Use it for countdowns and movement steps.
     */
    var deltaMs: Int = 0
        private set

    /**
     * Wall-clock epoch milliseconds at the start of the tick. Use it for absolute deadlines.
     */
    var now: Long = 0
        private set

    var tickNumber: Long = 0
        private set

    internal fun advance(deltaMs: Int, now: Long) {
        this.deltaMs = deltaMs
        this.now = now
        tickNumber++
    }

    companion object {
        const val MAX_DELTA_MS = 250
    }
}
