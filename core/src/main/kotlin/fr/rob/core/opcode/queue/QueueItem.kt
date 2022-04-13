package fr.rob.core.opcode.queue

import fr.rob.core.network.Packet
import fr.rob.core.network.v2.session.Session

data class QueueItem(val opcode: Int, val session: Session, val packet: Packet)
