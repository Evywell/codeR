package fr.rob.core.network.v2.session

interface SessionSocketInterface {
    fun getIp(): String
    fun send(data: Any)
    fun close()
    fun kick() {
        close()
    }
}
