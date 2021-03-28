package fr.rob.test.sandbox.opcode

import fr.rob.core.BaseApplication
import fr.rob.core.log.LoggerInterface
import fr.rob.core.opcode.OpcodeHandler

class NIOpcodeHandler(app: BaseApplication, logger: LoggerInterface) : OpcodeHandler(app, logger) {

    override fun initialize() {}
}
