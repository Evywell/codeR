package fr.rob.core.network.message

interface ResponseStackInterface {

    fun getResponse(requestId: String, timeoutMS: Long = 500): Any?

    fun responseReceived(requestId: String, response: Any?)
}
