package fr.rob.login.test.unit.sandbox.network

import fr.rob.core.network.Packet
import fr.rob.login.network.LoginSession
import java.util.ArrayList

class SpyingLoginSession : LoginSession() {

    val packetStack = ArrayList<Packet>()

    init {
        socket = SpyingLoginSocket(this)
    }

    fun getLastPacket(): Packet? {
        if (packetStack.isEmpty()) {
            return null
        }

        return packetStack[packetStack.size - 1]
    }
}
