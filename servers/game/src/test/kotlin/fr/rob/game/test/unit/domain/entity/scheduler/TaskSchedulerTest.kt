package fr.rob.game.test.unit.domain.entity.scheduler

import fr.rob.game.entity.scheduler.TaskGroup
import fr.rob.game.entity.scheduler.TaskScheduler
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class TaskSchedulerTest {
    private val scheduler = TaskScheduler()
    private val runs = ArrayList<String>()

    @Test
    fun `a task runs once its delay has elapsed`() {
        scheduler.schedule(100) { runs.add("a") }

        scheduler.update(99)
        assertTrue(runs.isEmpty())

        scheduler.update(1)
        assertEquals(listOf("a"), runs)

        scheduler.update(1_000)
        assertEquals(listOf("a"), runs)
    }

    @Test
    fun `due tasks run earliest first`() {
        scheduler.schedule(50) { runs.add("late") }
        scheduler.schedule(10) { runs.add("early") }

        scheduler.update(100)

        assertEquals(listOf("early", "late"), runs)
    }

    @Test
    fun `a repeated task keeps its cadence`() {
        scheduler.schedule(100) { runs.add("tick"); it.repeat() }

        repeat(10) { scheduler.update(30) }

        // 300 ms elapsed: runs at 100 (after 120), 200 (after 210) and 300
        assertEquals(3, runs.size)
    }

    @Test
    fun `a repeated task runs at most once per update`() {
        scheduler.schedule(100) { runs.add("tick"); it.repeat() }

        scheduler.update(1_000)

        assertEquals(1, runs.size)
    }

    @Test
    fun `repeat accepts a different delay`() {
        scheduler.schedule(100) { runs.add("tick"); it.repeat(10) }

        scheduler.update(100)
        scheduler.update(10)

        assertEquals(2, runs.size)
    }

    @Test
    fun `cancelled tasks don't run`() {
        val id = scheduler.schedule(10) { runs.add("by id") }
        scheduler.schedule(10, TaskGroup.COMBAT_SWING) { runs.add("by group") }
        scheduler.schedule(10, TaskGroup.VISIBILITY_REFRESH) { runs.add("kept") }

        scheduler.cancel(id)
        scheduler.cancelGroup(TaskGroup.COMBAT_SWING)
        scheduler.update(10)

        assertEquals(listOf("kept"), runs)
        assertFalse(scheduler.isScheduled(TaskGroup.COMBAT_SWING))
    }

    @Test
    fun `a task cancelled by an earlier task of the same update doesn't run`() {
        scheduler.schedule(5) { scheduler.cancelGroup(TaskGroup.COMBAT_SWING) }
        scheduler.schedule(10, TaskGroup.COMBAT_SWING) { runs.add("cancelled") }

        scheduler.update(10)

        assertTrue(runs.isEmpty())
    }

    @Test
    fun `a task scheduled while running waits for the next update`() {
        scheduler.schedule(10) { scheduler.schedule(0) { runs.add("nested") } }

        scheduler.update(10)
        assertTrue(runs.isEmpty())

        scheduler.update(0)
        assertEquals(listOf("nested"), runs)
    }

    @Test
    fun `cancelAll removes everything`() {
        scheduler.schedule(10, TaskGroup.COMBAT_SWING) { runs.add("a") }
        scheduler.schedule(10) { runs.add("b") }

        scheduler.cancelAll()
        scheduler.update(10)

        assertTrue(runs.isEmpty())
    }
}
