package fr.rob.login.test.feature.service.network

import fr.rob.core.network.Packet
import fr.rob.core.network.session.Session
import fr.rob.core.opcode.OpcodeHandler

class ServerQueue(private val opcodeHandler: OpcodeHandler) : Queue() {

    override fun runItem(item: QueueItem?) {
        if (item == null) {
            return
        }

        item as ServerQueueItem
        opcodeHandler.process(item.opcode, item.session, item.packet)
    }
}

data class ServerQueueItem(override val opcode: Int, val session: Session, override val packet: Packet) :
    QueueItem(opcode, packet)
