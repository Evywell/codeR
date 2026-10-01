package fr.rob.game.world

import kotlinx.coroutines.CoroutineDispatcher
import kotlin.coroutines.CoroutineContext

/**
 * Resumes coroutines on the world thread, during the next [WorldTaskQueue.dequeue] call.
 */
class WorldDispatcher(private val worldTaskQueue: WorldTaskQueue) : CoroutineDispatcher() {
    override fun dispatch(context: CoroutineContext, block: Runnable) {
        worldTaskQueue.enqueue { block.run() }
    }
}
