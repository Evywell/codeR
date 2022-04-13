package fr.rob.core.opcode

import fr.rob.core.network.Packet
import fr.rob.core.network.v2.session.Session

interface OpcodeHandlerInterface {

    fun process(opcode: Int, session: Session, packet: Packet)
    fun registerOpcode(opcode: Int, function: OpcodeFunction)
}
