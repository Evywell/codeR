package fr.rob.game.world.packet

import fr.raven.log.LoggerInterface
import fr.rob.core.network.thread.LockedQueue
import fr.rob.core.network.thread.LockedQueueConsumer
import fr.rob.game.player.session.GameSession
import fr.rob.game.world.function.WorldFunctionRegistry
import java.util.concurrent.ConcurrentHashMap

class WorldPacketQueue(
    private val worldPacketRegistry: WorldFunctionRegistry,
    private val logger: LoggerInterface,
) {
    private val queues = ConcurrentHashMap<GameSession, WorldQueue>()

    fun enqueue(packet: WorldPacket) {
        // compute() is atomic per key, so a concurrent prune in dequeue() can't drop this packet.
        queues.compute(packet.sender) { _, existing ->
            (existing ?: createQueue()).also { it.localQueue.addLast(packet) }
        }
    }

    fun dequeue() {
        val filter = WorldPacketFilter()

        queues.forEach { (session, queue) ->
            queue.consumer.consume(filter) { item ->
                val function = worldPacketRegistry.findFunction(item.opcode)

                if (function == null) {
                    logger.warning("Dropping packet with unknown opcode {} from account {}", item.opcode, session.accountId)
                    return@consume
                }

                try {
                    function.invoke(item.sender, item.opcode, item.message)
                } catch (e: Exception) {
                    logger.error("Handler for opcode {} failed for account {}", item.opcode, session.accountId, e)
                }
            }

            queues.computeIfPresent(session) { _, current -> if (current.localQueue.isEmpty()) null else current }
        }
    }

    fun removeQueue(session: GameSession) {
        queues.remove(session)
    }

    fun size(): Int = queues.size

    private fun createQueue(): WorldQueue {
        val localQueue = LockedQueue<WorldPacket>()
        return WorldQueue(localQueue, LockedQueueConsumer(200, localQueue))
    }

    data class WorldQueue(
        val localQueue: LockedQueue<WorldPacket>,
        val consumer: LockedQueueConsumer<WorldPacket>,
    )
}
