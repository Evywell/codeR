package fr.rob.core.test.cucumber

import fr.rob.core.misc.clock.IntervalTimer
import fr.rob.core.test.cucumber.service.Client
import fr.rob.core.test.cucumber.service.Message
import fr.rob.core.test.cucumber.service.MessageHolderInterface
import fr.rob.core.test.cucumber.service.checker.CheckerInterface
import fr.rob.core.test.cucumber.service.checker.UnresolvedCheckerTimeoutException

abstract class AbstractContext {

    lateinit var latestMessage: Message

    /**
     * Try to resolve a checker. If it did not succeed after {timeout} ms, an exception is thrown
     */
    fun resolveChecker(checker: CheckerInterface, messageHolder: MessageHolderInterface, timeout: Int = TIMEOUT_MS): Message? {
        val timer = IntervalTimer(timeout)
        var result = false

        var currentTime = System.currentTimeMillis()
        var previousTime = currentTime
        var deltaTime: Int
        var executionTime: Long

        do {
            currentTime = System.currentTimeMillis()
            deltaTime = (currentTime - previousTime).toInt()

            for (message in messageHolder.getMessages()) {
                result = checker.resolve(message)

                if (result) {
                    return message
                }
            }

            timer.update(deltaTime)

            executionTime = System.currentTimeMillis() - currentTime

            if (timer.passed()) {
                throw UnresolvedCheckerTimeoutException()
            }

            previousTime = currentTime

            if (executionTime < QUEUE_UPDATE_INTERVAL) {
                Thread.sleep(QUEUE_UPDATE_INTERVAL - executionTime)
            }
        } while (!result)

        return null
    }

    fun removeMessage(message: Message, client: Client) {
        client.messages.remove(message)
    }

    companion object {
        const val TIMEOUT_MS = 4000

        private const val QUEUE_UPDATE_PER_SECOND = 50
        const val QUEUE_UPDATE_INTERVAL = 1000 / QUEUE_UPDATE_PER_SECOND
    }
}
