package fr.rob.game.domain.opcode

import fr.rob.game.domain.log.LoggerInterface
import fr.rob.game.domain.network.Session
import fr.rob.game.domain.network.packet.Packet

open class OpcodeHandler(private val logger: LoggerInterface) {

    private val opcodeTable = HashMap<Int, OpcodeFunction>()

    fun process(opcode: Int, session: Session, packet: Packet) {
        val function = opcodeTable[opcode] ?: throw Exception("Cannot find opcode $opcode")

        val message = function.createMessageFromPacket(packet)

        function.call(session, message)
    }

    fun registerOpcode(opcode: Int, function: OpcodeFunction) {
        opcodeTable[opcode] = function
    }
}