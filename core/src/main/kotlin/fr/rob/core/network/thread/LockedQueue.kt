package fr.rob.core.network.thread

import fr.rob.core.network.Filter
import java.util.concurrent.locks.ReentrantLock

class LockedQueue<T> {

    private val internalQueue = ArrayDeque<T>()
    private val lock = ReentrantLock()

    fun addLast(item: T) {
        lock.lock()

        try {
            internalQueue.addLast(item)
        } finally {
            lock.unlock()
        }
    }

    fun next(filter: Filter<T>): T? {
        lock.lock()

        val result = getElementIfValid(filter)

        lock.unlock()

        return result
    }

    private fun getElementIfValid(filter: Filter<T>): T? {
        if (internalQueue.isEmpty()) {
            return null
        }

        val result = internalQueue.first()

        if (!filter.process(result)) {
            return null
        }

        internalQueue.removeFirst()

        return result
    }
}
