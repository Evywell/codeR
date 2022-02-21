package fr.rob.core.messaging.send

import com.google.protobuf.Message
import fr.rob.core.entities.MessagingProto

class MessageQueueDispatcher(transports: Array<TransportConfig>, routingItems: Array<QueueRouting>) :
    MessageQueueDispatcherInterface {
    private val senderRouting = HashMap<String, SenderInterface>()

    init {
        for (routing in routingItems) {
            val transport = getTransportByName(routing.transportName, transports)

            if (transport != null) {
                senderRouting[routing.messageType] = transport.sender
            }
        }
    }

    override fun dispatch(message: Message) {
        val sender = retrieveSender(message.javaClass.name)
        val envelope = MessagingProto.Envelope.newBuilder()
            .setEventName(message.javaClass.name)
            .setData(message.toByteString())
            .build()

        sender.send(envelope)
    }

    private fun retrieveSender(messageType: String): SenderInterface =
        senderRouting[messageType] ?: throw Exception("Cannot find routing for message type $messageType")

    private fun getTransportByName(name: String, transports: Array<TransportConfig>): TransportConfig? {
        for (transport in transports) {
            if (transport.name == name) {
                return transport
            }
        }

        return null
    }
}
