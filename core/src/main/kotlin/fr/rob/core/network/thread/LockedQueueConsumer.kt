package fr.rob.core.network.thread

import fr.rob.core.network.Filter

class LockedQueueConsumer<T>(private val maxItemToProcess: Int, private val queue: LockedQueue<T>) {

    fun consume(filter: Filter<T>, callable: (item: T) -> Unit) {
        var nextItm = queue.next(filter)
        var itemCount = 0

        while (nextItm != null) {
            ++itemCount
            callable(nextItm)

            if (itemCount > maxItemToProcess) {
                break
            }

            nextItm = queue.next(filter)
        }
    }
}
