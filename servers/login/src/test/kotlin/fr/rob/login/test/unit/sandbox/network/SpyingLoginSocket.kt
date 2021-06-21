package fr.rob.login.test.unit.sandbox.network

import fr.rob.core.network.Packet
import fr.rob.core.network.session.SessionSocketInterface

class SpyingLoginSocket(private val spyingLoginSession: SpyingLoginSession) : SessionSocketInterface {

    override fun getIp(): String {
        TODO("Not yet implemented")
    }

    override fun send(packet: Packet) {
        spyingLoginSession.packetStack.add(packet)
    }

    override fun close() {
        TODO("Not yet implemented")
    }
}
