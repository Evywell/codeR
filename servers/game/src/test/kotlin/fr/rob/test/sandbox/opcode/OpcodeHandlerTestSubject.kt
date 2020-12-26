package fr.rob.test.sandbox.opcode

import fr.rob.game.domain.opcode.OpcodeFunction
import fr.rob.game.domain.server.packet.Packet

abstract class OpcodeHandlerTestSubject(private val subject: Any) : OpcodeFunction() {

    override fun createMessageFromPacket(packet: Packet): Any = subject
}