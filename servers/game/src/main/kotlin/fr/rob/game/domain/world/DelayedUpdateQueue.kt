package fr.rob.game.domain.world

import fr.rob.core.network.NullFilter
import fr.rob.core.network.thread.LockedQueue
import fr.rob.core.network.thread.LockedQueueConsumer

class DelayedUpdateQueue {
    private val queue = LockedQueue<DelayedUpdateInterface>()
    private val queueConsumer = LockedQueueConsumer(10, queue)
    private val filter = NullFilter<DelayedUpdateInterface>()

    fun enqueueDelayedUpdate(delayedUpdate: DelayedUpdateInterface) {
        queue.addLast(delayedUpdate)
    }

    fun dequeue(deltaTime: Int) {
        queueConsumer.consume(filter) { item -> item.update(deltaTime) }
    }
}
