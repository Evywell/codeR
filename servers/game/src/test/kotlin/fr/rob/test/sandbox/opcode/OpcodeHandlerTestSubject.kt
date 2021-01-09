package fr.rob.test.sandbox.opcode

import fr.rob.game.domain.opcode.OpcodeFunction
import fr.rob.game.domain.network.packet.Packet

abstract class OpcodeHandlerTestSubject(private val subject: Any) : OpcodeFunction() {

    override fun createMessageFromPacket(packet: Packet): Any = subject
}