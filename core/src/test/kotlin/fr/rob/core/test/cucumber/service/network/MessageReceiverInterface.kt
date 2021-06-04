package fr.rob.core.test.cucumber.service.network

import fr.rob.core.network.Packet

interface MessageReceiverInterface {

    fun processMessage(opcode: Int, packet: Packet): Any?
}
