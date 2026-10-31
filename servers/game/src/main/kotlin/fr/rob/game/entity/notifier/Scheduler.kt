package fr.rob.game.entity.notifier

import fr.rob.core.misc.clock.IntervalTimer
import fr.rob.game.entity.guid.ObjectGuid

class Scheduler {
    private val timers = HashMap<ObjectGuid, IntervalTimer>()

    fun scheduleNotification(deltaTime: Int, scheduledItem: ScheduledItem) {
        if (!timers.containsKey(scheduledItem.identifier)) {
            invokeScheduledNotification(scheduledItem)
            timers[scheduledItem.identifier] = IntervalTimer(scheduledItem.delay)

            return
        }

        val timer = timers[scheduledItem.identifier] ?: return
        timer.update(deltaTime)

        if (timer.passed()) {
            timer.reset()
            invokeScheduledNotification(scheduledItem)
        }
    }

    fun unschedule(identifier: ObjectGuid) {
        timers.remove(identifier)
    }

    fun size(): Int = timers.size

    private fun invokeScheduledNotification(scheduledItem: ScheduledItem) {
        scheduledItem.function.invoke()
    }
}
