package fr.rob.core.network.v2

import fr.rob.core.network.message.ResponseStackInterface
import fr.rob.core.network.v2.session.Session
import fr.rob.core.network.v2.session.SessionSocketInterface

interface ClientInterface<T> {

    val responseStack: ResponseStackInterface

    fun onConnectionEstablished(session: Session)
    fun onPacketReceived(packet: T)

    fun send(message: Any)

    fun createSession(socket: SessionSocketInterface): Session
    fun open(process: ClientProcessInterface) {
        process.start()
    }
}
