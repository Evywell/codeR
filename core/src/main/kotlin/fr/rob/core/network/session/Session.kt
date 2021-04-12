package fr.rob.core.network.session

import fr.rob.core.network.Packet
import fr.rob.core.network.session.exception.UnauthenticatedSessionException

abstract class Session {

    var isAuthenticated: Boolean = false
    var userId: Int? = null

    abstract fun getIp(): String
    abstract fun send(packet: Packet)
    abstract fun close()

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
