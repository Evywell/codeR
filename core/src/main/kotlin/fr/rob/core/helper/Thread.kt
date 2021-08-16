package fr.rob.core.helper

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout

class Thread {

    companion object {
        fun waitFor(timeout: Long = DEFAULT_TIMEOUT, callback: () -> Boolean) {
            runBlocking {
                withTimeout(timeout) {
                    waitForCallback(callback)
                }
            }
        }

        private suspend fun waitForCallback(callback: () -> Boolean) {
            while (!callback()) {
                delay(50)
            }
        }

        private const val DEFAULT_TIMEOUT = 500L
    }
}
