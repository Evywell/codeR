package fr.rob.game.test.unit.domain.entity.notifier

import fr.rob.game.entity.guid.ObjectGuid
import fr.rob.game.entity.notifier.ScheduledItem
import fr.rob.game.entity.notifier.Scheduler
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class SchedulerTest {
    @Test
    fun `unschedule releases the timer`() {
        val scheduler = Scheduler()
        val guid = ObjectGuid(1u, 0)

        scheduler.scheduleNotification(0, ScheduledItem(250, guid) {})
        assertEquals(1, scheduler.size())

        scheduler.unschedule(guid)
        assertEquals(0, scheduler.size())
    }
}
