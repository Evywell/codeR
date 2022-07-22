package sandbox.checker

import fr.raven.proto.message.gateway.GatewayProto.Packet
import fr.rob.core.misc.clock.IntervalTimer
import sandbox.checker.exception.ResolverException
import sandbox.client.GatewayClient

class CheckerResolver(private val client: GatewayClient) {
    fun resolve(timeout: Int = TIMEOUT_MS, callback: (packet: Packet) -> Boolean) {
        val timer = IntervalTimer(timeout)

        var currentTime = System.currentTimeMillis()
        var previousTime = currentTime
        var deltaTime: Int
        var executionTime: Long

        do {
            currentTime = System.currentTimeMillis()
            deltaTime = (currentTime - previousTime).toInt()

            for (packet in client.packetStack) {
                if (callback(packet)) {
                    return
                }
            }

            timer.update(deltaTime)

            executionTime = System.currentTimeMillis() - currentTime

            if (timer.passed()) {
                throw ResolverException()
            }

            previousTime = currentTime

            if (executionTime < QUEUE_UPDATE_INTERVAL) {
                Thread.sleep(QUEUE_UPDATE_INTERVAL - executionTime)
            }
        } while (true)
    }

    companion object {
        const val TIMEOUT_MS = 4000

        private const val QUEUE_UPDATE_PER_SECOND = 50
        const val QUEUE_UPDATE_INTERVAL = 1000 / QUEUE_UPDATE_PER_SECOND
    }
}
