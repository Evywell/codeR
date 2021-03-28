package fr.rob.game.domain.opcode

import fr.rob.core.BaseApplication
import fr.rob.core.log.LoggerInterface
import fr.rob.core.opcode.OpcodeHandler
import fr.rob.core.process.ProcessManager

class ClientOpcodeHandler(private val processManager: ProcessManager, app: BaseApplication, logger: LoggerInterface) :
    OpcodeHandler(logger) {

    override fun initialize() {
    }
}
