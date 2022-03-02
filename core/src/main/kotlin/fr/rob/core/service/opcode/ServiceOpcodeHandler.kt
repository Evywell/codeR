package fr.rob.core.service.opcode

import fr.raven.log.LoggerInterface
import fr.rob.core.opcode.OpcodeHandler
import fr.rob.core.service.Action

class ServiceOpcodeHandler(private val actions: Collection<Action>, logger: LoggerInterface) : OpcodeHandler(logger) {
    override fun initialize() {
        for (action in actions) {
            registerOpcode(action.opcode, action.function)
        }
    }
}
