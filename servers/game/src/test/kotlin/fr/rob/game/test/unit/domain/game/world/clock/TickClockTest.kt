package fr.rob.game.test.unit.domain.game.world.clock

import fr.rob.game.test.unit.tools.ManualGameClock
import fr.rob.game.world.clock.TickClock
import fr.rob.game.world.clock.WorldTime
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TickClockTest {
    private val clock = ManualGameClock()
    private val worldTime = WorldTime()
    private val tickClock = TickClock(clock, worldTime)

    @Test
    fun `delta is the monotonic time elapsed since the previous tick`() {
        clock.advance(20)
        tickClock.advance()
        assertEquals(20, worldTime.deltaMs)

        clock.advance(35)
        tickClock.advance()
        assertEquals(35, worldTime.deltaMs)
    }

    @Test
    fun `delta is clamped after a long pause`() {
        clock.advance(10_000)
        tickClock.advance()

        assertEquals(WorldTime.MAX_DELTA_MS, worldTime.deltaMs)
    }

    @Test
    fun `wall clock adjustments do not affect the delta`() {
        clock.adjustWallClock(-3_600_000)
        clock.advance(20)
        tickClock.advance()

        assertEquals(20, worldTime.deltaMs)
    }

    @Test
    fun `now is the wall clock and tick numbers increase`() {
        clock.advance(20)
        tickClock.advance()
        assertEquals(1, worldTime.tickNumber)

        clock.advance(20)
        tickClock.advance()
        assertEquals(2, worldTime.tickNumber)
        assertEquals(clock.epochMillis(), worldTime.now)
    }
}
