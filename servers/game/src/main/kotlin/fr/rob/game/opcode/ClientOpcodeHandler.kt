package fr.rob.game.opcode

import fr.rob.core.log.LoggerInterface
import fr.rob.core.opcode.OpcodeHandler
import fr.rob.core.process.ProcessManager

class ClientOpcodeHandler(private val processManager: ProcessManager, logger: LoggerInterface) :
    OpcodeHandler(logger) {

    override fun initialize() { }
}
