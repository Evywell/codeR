package fr.rob.core.test.cucumber.service.checker

import fr.rob.core.test.cucumber.service.Message

class OpcodeChecker(private val opcode: Int) : CheckerInterface {

    override fun resolve(message: Message): Boolean = opcode == message.opcode
}
