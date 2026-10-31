package fr.rob.game.world.clock

interface GameClock {
    /**
     * Monotonic milliseconds, only meaningful as a difference between two calls. Use it to measure durations.
     */
    fun monotonicMillis(): Long

    /**
     * Wall-clock epoch milliseconds. Use it for absolute deadlines that outlive the process.
     */
    fun epochMillis(): Long
}
