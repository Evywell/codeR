package fr.rob.core.network.v2

import com.google.protobuf.Message
import fr.rob.core.entities.NetworkProto
import fr.rob.core.network.Packet
import fr.rob.core.network.message.ResponseStackInterface
import fr.rob.core.network.session.Session

interface ClientInterface {

    val responseStack: ResponseStackInterface

    fun onConnectionEstablished(session: Session)
    fun onPacketReceived(packet: Packet)

    fun send(packet: Packet)
    fun sendSyncMessage(opcode: Int, message: Message): Any?
    fun sendSync(opcode: Int, request: NetworkProto.Request): Any?

    fun createSession(): Session
    fun open(process: ClientProcessInterface) {
        process.start()
    }
}
