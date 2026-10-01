package fr.rob.game.world

import fr.raven.log.LoggerInterface
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Single entry point to run asynchronous logic from the world.
 *
 * - Code inside [launch] runs on the world thread.
 * - Code inside [database] runs on the database worker, then resumes on the world thread.
 */
class GameCoroutines(
    worldDispatcher: CoroutineDispatcher,
    private val databaseDispatcher: CoroutineDispatcher,
    private val logger: LoggerInterface? = null,
    private val onClose: () -> Unit = {},
) : AutoCloseable {
    private val exceptionHandler = CoroutineExceptionHandler { context, throwable ->
        logger?.error("Coroutine '${context[CoroutineName]?.name}' failed: ${throwable.stackTraceToString()}")
    }

    private val scope = CoroutineScope(SupervisorJob() + worldDispatcher + exceptionHandler)

    fun launch(name: String, block: suspend CoroutineScope.() -> Unit): Job =
        scope.launch(CoroutineName(name), block = block)

    suspend fun <T> database(block: () -> T): T = withContext(databaseDispatcher) { block() }

    override fun close() {
        scope.cancel()
        onClose()
    }
}
