package fr.rob.core.opcode

import fr.rob.core.network.Packet
import fr.rob.core.network.v2.session.Session

abstract class OpcodeFunction(val authenticationNeeded: Boolean = true) {

    /**
     * Called when the opcode is trigger
     */
    abstract fun call(session: Session, message: Any)

    /**
     * Creates a message from a packet and returns it
     * The message represents an easy to use data object (such as an entity)
     */
    abstract fun createMessageFromPacket(packet: Packet): Any
}
