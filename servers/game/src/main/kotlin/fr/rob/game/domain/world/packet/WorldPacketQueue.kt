package fr.rob.game.domain.world.packet

import fr.rob.core.network.thread.LockedQueue
import fr.rob.core.network.thread.LockedQueueConsumer
import fr.rob.game.domain.world.function.WorldFunctionRegistry

class WorldPacketQueue(private val worldPacketRegistry: WorldFunctionRegistry) {
    private val queues = HashMap<Int, WorldQueue>()

    fun enqueue(packet: WorldPacket) {
        val queue = queues.getOrPut(packet.sender.accountId) {
            val localQueue = LockedQueue<WorldPacket>()
            WorldQueue(localQueue, LockedQueueConsumer(200, localQueue))
        }
        queue.localQueue.addLast(packet)
    }

    fun dequeue() {
        val filter = WorldPacketFilter()

        queues.forEach { (_, queue) ->
            queue.consumer.consume(filter) { item ->
                worldPacketRegistry.getFunction(item.opcode).invoke(item.sender, item.opcode, item.message)
            }
        }
    }

    data class WorldQueue(
        val localQueue: LockedQueue<WorldPacket>,
        val consumer: LockedQueueConsumer<WorldPacket>,
    )
}
