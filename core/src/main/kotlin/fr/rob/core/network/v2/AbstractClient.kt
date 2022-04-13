package fr.rob.core.network.v2

import com.google.protobuf.Message
import fr.rob.core.entities.NetworkProto
import fr.rob.core.network.Packet
import fr.rob.core.network.message.ResponseStack
import fr.rob.core.network.v2.session.Session
import org.apache.commons.lang3.RandomStringUtils

abstract class AbstractClient<T> : ClientInterface<T> {
    lateinit var session: Session
    override val responseStack = ResponseStack()

    override fun sendSyncMessage(opcode: Int, message: Message): Any? {
        val request = createRequest(message)

        return sendSync(opcode, request)
    }

    override fun send(message: Any) {
        session.send(message)
    }

    override fun sendSync(opcode: Int, request: NetworkProto.Request): Any? {
        send(Packet(opcode, request.toByteArray()))

        return responseStack.getResponse(request)
    }

    protected fun createRequest(message: Message): NetworkProto.Request {
        val id = RandomStringUtils.randomAlphanumeric(8)
        val data = com.google.protobuf.Any.pack(message)

        return NetworkProto.Request.newBuilder()
            .setId(id)
            .setData(data)
            .build()
    }
}
