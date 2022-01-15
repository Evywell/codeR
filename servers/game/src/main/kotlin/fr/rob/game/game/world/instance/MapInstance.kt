package fr.rob.game.game.world.instance

import fr.rob.core.network.thread.LockedQueue
import fr.rob.core.network.thread.LockedQueueConsumer
import fr.rob.game.game.event.Event
import fr.rob.game.game.event.EventScheduler
import fr.rob.game.game.world.map.Map

/**
 * We use MapInstance instead of Instance to avoid confusion with the entity
 */
class MapInstance(val id: Int, val map: Map) {

    private val eventQueue = LockedQueue<Event>()
    private val eventQueueConsumer = LockedQueueConsumer(20, eventQueue)
    val eventScheduler = EventScheduler()

    fun update(deltaTime: Int) {
        eventScheduler.update(deltaTime)
    }
}
