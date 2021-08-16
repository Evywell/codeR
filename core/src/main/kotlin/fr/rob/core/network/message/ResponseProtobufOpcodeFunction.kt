package fr.rob.core.network.message

import com.google.protobuf.Message
import fr.rob.core.entities.NetworkProto
import fr.rob.core.network.session.Session
import fr.rob.core.opcode.ProtobufOpcodeFunction

abstract class ResponseProtobufOpcodeFunction(
    private val responseStack: ResponseStackInterface,
    authenticationNeeded: Boolean = true
) :
    ProtobufOpcodeFunction(authenticationNeeded) {

    abstract fun getDataType(): Message
    abstract fun handleResponse(session: Session, response: Message?): Any?

    override fun getMessageType(): Message = NetworkProto.Response.getDefaultInstance()

    override fun call(session: Session, message: Any) {
        message as NetworkProto.Response

        val data = message.data.unpack(getDataType().javaClass)

        responseStack.responseReceived(message.requestId, handleResponse(session, data))
    }
}
