package fr.rob.test.sandbox.opcode

import fr.rob.game.domain.network.packet.Packet
import fr.rob.game.domain.network.session.Session
import fr.rob.game.domain.opcode.OpcodeFunction

class DoNothingAuthenticatedOpcodeFunction(private val subject: Any): OpcodeFunction() {

    override fun call(session: Session, message: Any) { }

    override fun createMessageFromPacket(packet: Packet): Any = subject
}
