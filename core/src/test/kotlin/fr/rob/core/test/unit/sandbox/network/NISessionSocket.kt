package fr.rob.core.test.unit.sandbox.network

import fr.rob.core.network.v2.session.SessionSocketInterface

class NISessionSocket : SessionSocketInterface {

    override fun getIp(): String = "127.0.0.1"
    override fun send(data: Any) {}

    override fun close() {}
}
