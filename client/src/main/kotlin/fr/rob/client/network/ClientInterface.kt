package fr.rob.client.network

import com.google.protobuf.Message
import fr.rob.core.entities.NetworkProto
import fr.rob.core.network.Packet
import fr.rob.core.network.message.ResponseStackInterface
import org.apache.commons.lang3.RandomStringUtils

interface ClientInterface {

    val responseStack: ResponseStackInterface

    fun open()
    fun send(packet: Packet)
    fun sendSync(opcode: Int, request: NetworkProto.Request): Any?

    fun createRequest(message: Message): NetworkProto.Request {
        val id = generateRequestId()
        val data = com.google.protobuf.Any.pack(message)

        return NetworkProto.Request.newBuilder()
            .setId(id)
            .setData(data)
            .build()
    }

    private fun generateRequestId(): String {
        return getIncrement().toString() + "-" + RandomStringUtils.randomAlphanumeric(ID_LENGTH)
    }

    companion object {
        private var INTERNAL_INCREMENT = 0
        private const val ID_LENGTH = 8

        private fun getIncrement(): Int = ++INTERNAL_INCREMENT
    }
}
