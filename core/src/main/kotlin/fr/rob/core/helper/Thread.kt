package fr.rob.core.helper

import fr.rob.core.exception.TimeoutException
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout

class Thread {

    companion object {
        fun waitFor(timeout: Long = DEFAULT_TIMEOUT, callback: () -> Boolean) {
            runBlocking {
                try {
                    withTimeout(timeout) {
                        waitForCallback(callback)
                    }
                } catch (e: TimeoutCancellationException) {
                    // Throw the exception in the current thread to have a better stacktrace
                    throw TimeoutException(e.message!!)
                }
            }
        }

        private suspend fun waitForCallback(callback: () -> Boolean) {
            while (!callback()) {
                delay(TIME_BETWEEN_LOOPS)
            }
        }

        private const val TIME_BETWEEN_LOOPS = 50L
        const val DEFAULT_TIMEOUT = 500L
    }
}
