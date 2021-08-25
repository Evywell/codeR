package fr.rob.core.network.message

import fr.rob.core.entities.NetworkProto

interface ResponseStackInterface {

    fun getResponse(request: NetworkProto.Request, timeoutMS: Long = 500): Any?

    fun responseReceived(requestId: String, response: Any?)
}
