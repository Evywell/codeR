package fr.rob.test.sandbox.opcode

import fr.rob.core.network.Packet
import fr.rob.core.network.session.Session
import fr.rob.core.opcode.OpcodeFunction

class DoNothingAuthenticatedOpcodeFunction(private val subject: Any): OpcodeFunction() {

    override fun call(session: Session, message: Any) { }

    override fun createMessageFromPacket(packet: Packet): Any = subject
}