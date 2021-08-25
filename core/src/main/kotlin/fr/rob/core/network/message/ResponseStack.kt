package fr.rob.core.network.message

import fr.rob.core.entities.NetworkProto
import fr.rob.core.helper.Thread

class ResponseStack : ResponseStackInterface {

    private val stack = HashMap<String, Any?>()

    override fun getResponse(request: NetworkProto.Request, timeoutMS: Long): Any? {
        Thread.waitFor(timeoutMS) {
            stack.containsKey(request.id)
        }

        val value = stack[request.id]

        stack.remove(request.id)

        return value
    }

    override fun responseReceived(requestId: String, response: Any?) {
        stack[requestId] = response
    }
}
