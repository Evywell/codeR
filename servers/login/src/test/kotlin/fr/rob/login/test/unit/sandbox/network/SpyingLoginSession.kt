package fr.rob.login.test.unit.sandbox.network

import fr.rob.core.network.Packet
import fr.rob.login.network.LoginSession

class SpyingLoginSession(private val internalSocket: SpyingLoginSocket) : LoginSession(internalSocket) {
    fun getLastPacket(): Packet? = internalSocket.getLastPacket()
}
