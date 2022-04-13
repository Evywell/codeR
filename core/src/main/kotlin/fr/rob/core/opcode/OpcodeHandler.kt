package fr.rob.core.opcode

import fr.raven.log.LoggerInterface
import fr.rob.core.network.Packet
import fr.rob.core.network.v2.session.Session
import fr.rob.core.security.authentication.UnauthenticatedException

abstract class OpcodeHandler(private val logger: LoggerInterface) : OpcodeHandlerInterface {

    private val opcodeTable = HashMap<Int, OpcodeFunction>()

    @Throws(UnauthenticatedException::class, Exception::class)
    override fun process(opcode: Int, session: Session, packet: Packet) {
        logger.debug("Processing opcode [$opcode]")

        val function = opcodeTable[opcode] ?: throw Exception("Cannot find opcode $opcode")

        if (function.authenticationNeeded && !session.isAuthenticated) {
            session.kick()

            throw UnauthenticatedException(
                "The session MUST be authenticated for opcode $opcode, session closed",
                logger
            )
        }

        val message = function.createMessageFromPacket(packet)

        function.call(session, message)
    }

    override fun registerOpcode(opcode: Int, function: OpcodeFunction) {
        opcodeTable[opcode] = function
    }

    abstract fun initialize()

    fun getOpcodeFunction(opcode: Int): OpcodeFunction {
        return opcodeTable[opcode]!!
    }
}
