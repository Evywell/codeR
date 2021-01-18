package fr.rob.test.sandbox.opcode

import fr.rob.core.BaseApplication
import fr.rob.game.domain.log.LoggerInterface
import fr.rob.game.domain.opcode.OpcodeHandler

class NIOpcodeHandler(app: BaseApplication, logger: LoggerInterface) : OpcodeHandler(app, logger) {

    override fun initialize() {}
}
