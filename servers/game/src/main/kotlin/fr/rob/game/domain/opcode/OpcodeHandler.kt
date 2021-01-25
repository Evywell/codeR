package fr.rob.game.domain.opcode

import fr.rob.game.domain.log.LoggerInterface
import fr.rob.game.domain.network.packet.Packet
import fr.rob.game.domain.network.session.Session
import fr.rob.game.domain.security.authentication.UnauthenticatedException

open class OpcodeHandler(private val logger: LoggerInterface) {

    private val opcodeTable = HashMap<Int, OpcodeFunction>()

    @Throws(UnauthenticatedException::class, Exception::class)
    fun process(opcode: Int, session: Session, packet: Packet) {
        logger.debug("Processing opcode [$opcode]")

        val function = opcodeTable[opcode] ?: throw Exception("Cannot find opcode $opcode")

        if (function.authenticationNeeded && !session.isAuthenticated) {
            session.close()

            throw UnauthenticatedException(
                "The session MUST be authenticated for opcode $opcode, session closed",
                logger
            )
        }

        val message = function.createMessageFromPacket(packet)

        function.call(session, message)
    }

    fun registerOpcode(opcode: Int, function: OpcodeFunction) {
        opcodeTable[opcode] = function
    }

    fun getOpcodeFunction(opcode: Int): OpcodeFunction {
        return opcodeTable[opcode]!!
    }
}
