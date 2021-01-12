package fr.rob.test.sandbox.opcode

import fr.rob.game.domain.network.packet.Packet
import fr.rob.game.domain.opcode.OpcodeFunction

abstract class OpcodeHandlerTestSubject(private val subject: Any) : OpcodeFunction(false) {

    override fun createMessageFromPacket(packet: Packet): Any = subject
}
