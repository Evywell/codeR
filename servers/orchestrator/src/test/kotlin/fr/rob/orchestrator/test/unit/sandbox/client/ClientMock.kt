package fr.rob.orchestrator.test.unit.sandbox.client

import fr.rob.client.network.ClientInterface
import fr.rob.core.entities.NetworkProto
import fr.rob.core.network.Packet
import fr.rob.core.network.message.ResponseStackInterface

class ClientMock : ClientInterface {

    override val responseStack: ResponseStackInterface = ResponseStackMock()

    private val responseStore = HashMap<Int, Any?>()

    override fun open() {
        // Nothing to do here
    }

    override fun send(packet: Packet) {
        TODO("Not yet implemented")
    }

    override fun sendSync(opcode: Int, request: NetworkProto.Request): Any? {
        return responseStore[opcode]
    }

    fun whenSendMessage(opcode: Int, response: Any?) {
        responseStore[opcode] = response
    }
}
