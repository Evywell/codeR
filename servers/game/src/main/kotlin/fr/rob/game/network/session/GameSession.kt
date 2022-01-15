package fr.rob.game.network.session

import fr.rob.core.network.Filter
import fr.rob.core.network.Packet
import fr.rob.core.network.session.Session
import fr.rob.core.network.thread.LockedQueue
import fr.rob.core.network.thread.LockedQueueConsumer
import fr.rob.core.opcode.OpcodeHandlerInterface

class GameSession(private val opcodeHandler: OpcodeHandlerInterface) : Session() {

    private val queue = LockedQueue<Packet>()
    private val queueConsumer = LockedQueueConsumer(MAX_PACKET_PROCESSING_ON_UPDATE, queue)

    fun update(deltaTime: Int, filter: Filter<Packet>) {
        queueConsumer.consume(filter) { item ->
            opcodeHandler.process(item.readOpcode(), this, item)
        }
    }

    companion object {
        private const val MAX_PACKET_PROCESSING_ON_UPDATE = 200
    }
}
