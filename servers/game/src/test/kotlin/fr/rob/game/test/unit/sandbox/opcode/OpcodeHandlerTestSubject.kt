package fr.rob.game.test.unit.sandbox.opcode

import fr.rob.core.network.Packet
import fr.rob.core.opcode.OpcodeFunction

abstract class OpcodeHandlerTestSubject(private val subject: Any) : OpcodeFunction(false) {

    override fun createMessageFromPacket(packet: Packet): Any = subject
}
