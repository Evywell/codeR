package fr.rob.game.domain.opcode

import fr.rob.game.domain.network.packet.Packet
import fr.rob.game.domain.network.session.Session

abstract class OpcodeFunction {

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
