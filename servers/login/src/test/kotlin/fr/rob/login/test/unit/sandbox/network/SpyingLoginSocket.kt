package fr.rob.login.test.unit.sandbox.network

import fr.rob.core.network.Packet
import fr.rob.core.network.v2.session.SessionSocketInterface
import java.util.ArrayList

class SpyingLoginSocket : SessionSocketInterface {

    private val packetStack = ArrayList<Packet>()

    fun getLastPacket(): Packet? {
        if (packetStack.isEmpty()) {
            return null
        }

        return packetStack[packetStack.size - 1]
    }

    override fun getIp(): String {
        TODO("Not yet implemented")
    }

    override fun send(data: Any) {
        packetStack.add(data as Packet)
    }

    override fun close() {
        TODO("Not yet implemented")
    }
}
