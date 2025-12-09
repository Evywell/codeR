package fr.rob.core.network.v2

import fr.rob.core.network.v2.session.Session

abstract class AbstractClient<T> : ClientInterface<T> {
    lateinit var session: Session

    fun isSessionInitialized(): Boolean = ::session.isInitialized

    override fun send(message: Any) {
        session.send(message)
    }

    override fun onConnectionClosed() {}
}
