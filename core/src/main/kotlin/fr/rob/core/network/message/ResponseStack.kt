package fr.rob.core.network.message

import fr.rob.core.helper.Thread

class ResponseStack : ResponseStackInterface {

    private val stack = HashMap<String, Any?>()

    override fun getResponse(requestId: String, timeoutMS: Long): Any? {
        Thread.waitFor(timeoutMS) {
            stack.containsKey(requestId)
        }

        val value = stack[requestId]

        stack.remove(requestId)

        return value
    }

    override fun responseReceived(requestId: String, response: Any?) {
        stack[requestId] = response
    }
}
