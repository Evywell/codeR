package fr.rob.core.network.v2.session

import fr.rob.core.network.session.exception.UnauthenticatedSessionException

open class Session(val socket: SessionSocketInterface) {
    var isAuthenticated: Boolean = false
    var accountId: Int? = null

    fun getIp(): String = socket.getIp()

    fun send(message: Any) {
        socket.send(message)
    }

    fun close() {
        socket.close()
    }

    fun kick() {
        socket.kick()
    }

    fun isLocal(): Boolean {
        return getIp() == LOCALHOST_IPV4 || getIp() == LOCALHOST_IPV6
    }

    open fun isAuthenticatedOrThrowException() {
        if (!isAuthenticated) {
            throw UnauthenticatedSessionException()
        }
    }

    companion object {
        const val LOCALHOST_IPV4 = "127.0.0.1"
        const val LOCALHOST_IPV6 = "::1"
    }
}
