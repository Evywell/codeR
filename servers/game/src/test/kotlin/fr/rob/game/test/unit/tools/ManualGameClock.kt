package fr.rob.game.test.unit.tools

import fr.rob.game.world.clock.GameClock

class ManualGameClock(
    private var monotonic: Long = 0,
    private var epoch: Long = 1_700_000_000_000,
) : GameClock {
    override fun monotonicMillis(): Long = monotonic

    override fun epochMillis(): Long = epoch

    fun advance(ms: Long) {
        monotonic += ms
        epoch += ms
    }

    /**
     * Moves the wall clock only, like an NTP adjustment.
     */
    fun adjustWallClock(ms: Long) {
        epoch += ms
    }
}
