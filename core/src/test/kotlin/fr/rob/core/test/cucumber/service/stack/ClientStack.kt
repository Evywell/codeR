package fr.rob.core.test.cucumber.service.stack

import fr.rob.core.test.cucumber.service.Client
import fr.rob.core.test.cucumber.service.Message
import fr.rob.core.test.cucumber.service.network.MessageReceiverInterface

class ClientStack(private val client: Client, private val messageReceiver: MessageReceiverInterface) : AbstractStack() {

    override fun processItem(item: StackItem) {
        val message = messageReceiver.processMessage(item.opcode, item.packet!!)

        client.messages.add(Message(item.opcode, message))
    }
}
