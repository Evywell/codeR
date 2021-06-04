package fr.rob.core.test.cucumber.service.stack

import fr.rob.core.opcode.OpcodeHandler

class ServerStack(private val opcodeHandler: OpcodeHandler) : AbstractStack() {

    override fun processItem(item: StackItem) {
        opcodeHandler.process(item.opcode, item.session!!, item.packet!!)
    }
}
