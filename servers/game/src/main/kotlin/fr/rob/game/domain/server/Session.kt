package fr.rob.game.domain.server

import fr.rob.game.domain.server.packet.Packet

abstract class Session(protected val gameServer: GameServer) {

    abstract fun getIp(): String
    abstract fun send(packet: Packet)

    fun isLocal(): Boolean {
        return getIp() == LOCALHOST_IPV4 || getIp() == LOCALHOST_IPV6
    }

    companion object {
        const val LOCALHOST_IPV4 = "127.0.0.1"
        const val LOCALHOST_IPV6 = "::1"
    }
}