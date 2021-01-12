package fr.rob.game.domain.network.session

import fr.rob.game.domain.network.GameServer
import fr.rob.game.domain.network.packet.Packet

abstract class Session(protected val gameServer: GameServer) {

    var isAuthenticated: Boolean = false

    abstract fun getIp(): String
    abstract fun send(packet: Packet)
    abstract fun close()

    fun isLocal(): Boolean {
        return getIp() == LOCALHOST_IPV4 || getIp() == LOCALHOST_IPV6
    }

    companion object {
        const val LOCALHOST_IPV4 = "127.0.0.1"
        const val LOCALHOST_IPV6 = "::1"
    }
}
