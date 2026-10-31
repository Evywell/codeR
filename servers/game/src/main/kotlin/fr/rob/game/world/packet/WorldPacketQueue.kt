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
                val entry = worldPacketRegistry.findItem(item.opcode)

                if (entry == null) {
                    reject(session, item, "unknown opcode")
                    return@consume
                }

                if (session.state != entry.requiredState) {
                    reject(session, item, "requires state ${entry.requiredState}, session is ${session.state}")
                    return@consume
                }

                try {
                    entry.function.invoke(item.sender, item.opcode, item.message)
                } catch (e: Exception) {
                    logger.error("Handler for opcode {} failed for account {}", item.opcode, session.accountId, e)
                }
            }

            queues.computeIfPresent(session) { _, current -> if (current.localQueue.isEmpty()) null else current }
        }
    }

    private fun reject(session: GameSession, packet: WorldPacket, reason: String) {
        val count = session.recordRejectedPacket()

        logger.warning("Dropping packet with opcode {} from account {}: {}", packet.opcode, session.accountId, reason)

        // @todo ask the gateway to disconnect the player instead of only logging
        if (count == REJECTED_PACKET_THRESHOLD) {
            logger.warning("Account {} reached {} rejected packets", session.accountId, REJECTED_PACKET_THRESHOLD)
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

    companion object {
        const val REJECTED_PACKET_THRESHOLD = 50
    }
}
