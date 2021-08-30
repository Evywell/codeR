package fr.rob.core.network.message

import fr.rob.core.entities.NetworkProto
import fr.rob.core.helper.Thread.Companion.DEFAULT_TIMEOUT

interface ResponseStackInterface {

    fun getResponse(request: NetworkProto.Request, timeoutMS: Long = DEFAULT_TIMEOUT): Any?

    fun responseReceived(requestId: String, response: Any?)
}
