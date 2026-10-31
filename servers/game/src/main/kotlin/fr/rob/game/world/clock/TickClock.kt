package fr.rob.game.world.clock

/**
 * Advances [WorldTime] from a [GameClock], once per world tick.
 */
class TickClock(private val clock: GameClock, private val worldTime: WorldTime) {
    private var previousMonotonic = clock.monotonicMillis()

    fun advance() {
        val currentMonotonic = clock.monotonicMillis()
        val delta = (currentMonotonic - previousMonotonic).coerceIn(0L, WorldTime.MAX_DELTA_MS.toLong())

        previousMonotonic = currentMonotonic

        worldTime.advance(delta.toInt(), clock.epochMillis())
    }
}
