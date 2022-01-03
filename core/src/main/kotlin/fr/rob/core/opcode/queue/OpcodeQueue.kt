package fr.rob.core.opcode.queue

import fr.rob.core.network.Packet
import fr.rob.core.network.session.Session
import fr.rob.core.opcode.OpcodeHandlerInterface
import java.util.concurrent.ConcurrentLinkedDeque

class OpcodeQueue(private val opcodeHandler: OpcodeHandlerInterface) {

    private val queue = ConcurrentLinkedDeque<QueueItem>()

    fun putInQueue(opcode: Int, session: Session, packet: Packet) {
        queue.addLast(QueueItem(opcode, session, packet))
    }

    fun processQueue() {
        while (queue.isNotEmpty()) {
            val item = queue.pollFirst()

            if (item != null) {
                opcodeHandler.process(item.opcode, item.session, item.packet)
            }
        }
    }
}
