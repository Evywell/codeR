package fr.rob.core.network.message

import com.google.protobuf.Message
import fr.rob.core.entities.NetworkProto
import fr.rob.core.network.Packet
import fr.rob.core.network.v2.session.Session
import fr.rob.core.opcode.ProtobufOpcodeFunction
import com.google.protobuf.Any as ProtoAny

abstract class RequestProtobufOpcodeFunction(
    authenticationNeeded: Boolean = true
) :
    ProtobufOpcodeFunction(authenticationNeeded) {

    abstract fun getDataType(): Message
    abstract fun callWithResponse(session: Session, message: Message): ResponseMessage

    override fun getMessageType(): Message = NetworkProto.Request.getDefaultInstance()

    override fun call(session: Session, message: Any) {
        message as NetworkProto.Request

        val data = message.data.unpack(getDataType().javaClass)
        val responseData = callWithResponse(session, data)

        val response = NetworkProto.Response.newBuilder()
            .setData(ProtoAny.pack(responseData.message))
            .setRequestId(message.id)
            .build()

        session.send(Packet(responseData.opcode, response.toByteArray()))
    }
}
