package fr.rob.core.network.session

import fr.rob.core.network.Packet
import fr.rob.core.network.session.exception.UnauthenticatedSessionException

open class Session {

    var isAuthenticated: Boolean = false
    var userId: Int? = null
    lateinit var socket: SessionSocketInterface

    fun getIp(): String = socket.getIp()

    fun send(packet: Packet) {
        socket.send(packet)
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
