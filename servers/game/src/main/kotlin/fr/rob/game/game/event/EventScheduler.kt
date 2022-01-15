package fr.rob.game.game.event

import com.google.common.collect.LinkedListMultimap
import com.google.common.collect.Multimap

class EventScheduler {

    private var globalTime: Long = 0
    private val eventQueue: Multimap<Long, Event> = LinkedListMultimap.create()
    private val tmpEventQueue: Multimap<Long, Event> = LinkedListMultimap.create()

    fun update(deltaTime: Int) {
        globalTime += deltaTime

        val eventIterator = eventQueue.entries().iterator()

        while (eventIterator.hasNext()) {
            val events = eventIterator.next()
            val eventTimeScheduled = events.key

            if (eventTimeScheduled > globalTime) {
                continue
            }

            val event = events.value
            event.execute()

            eventIterator.remove()
        }

        eventQueue.putAll(tmpEventQueue)
        tmpEventQueue.clear()
    }

    fun addEvent(event: Event, time: Long) {
        event.addTime = globalTime
        event.execTime = globalTime + time

        tmpEventQueue.put(event.execTime, event)
    }
}
