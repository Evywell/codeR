package fr.rob.orchestrator.test.unit.sandbox.client

import fr.rob.core.entities.NetworkProto
import fr.rob.core.network.message.ResponseStackInterface

class ResponseStackMock : ResponseStackInterface {

    override fun getResponse(request: NetworkProto.Request, timeoutMS: Long): Any? {
        TODO("Not yet implemented")
    }

    override fun responseReceived(requestId: String, response: Any?) {
        TODO("Not yet implemented")
    }
}
