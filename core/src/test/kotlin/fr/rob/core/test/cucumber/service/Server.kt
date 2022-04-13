package fr.rob.core.test.cucumber.service

import fr.rob.core.network.Packet
import fr.rob.core.network.Server
import fr.rob.core.network.v2.session.Session
import fr.rob.core.opcode.OpcodeHandler
import fr.rob.core.test.cucumber.service.stack.ServerStack
import fr.rob.core.test.cucumber.service.stack.StackItem

class Server(opcodeHandler: OpcodeHandler) : Server() {

    private var stack: ServerStack = ServerStack(opcodeHandler)

    init {
        stack.start()
    }

    fun receiveMessage(session: Session, packet: Packet) {
        stack.pushItem(StackItem(packet.readOpcode(), session, packet))
    }

    fun stop() {
        stack.shutdown()
    }
}
