package fr.raven.messaging.send

import com.google.protobuf.Message
import fr.raven.messaging.message.MessagingProto

class MessageQueueDispatcher(transports: Array<TransportConfig>, routingItems: Array<QueueRouting>) :
    MessageQueueDispatcherInterface {
    private val senderRouting = HashMap<String, MutableList<SenderInterface>>()

    init {
        for (routing in routingItems) {
            val transport = getTransportByName(routing.transportName, transports)

            setRoutingSender(routing.messageType, transport.sender)
        }
    }

    override fun dispatch(message: Message) {
        val senders = retrieveSenders(message.javaClass.name)
        val envelope = MessagingProto.Envelope.newBuilder()
            .setEventName(message.javaClass.name)
            .setData(message.toByteString())
            .build()

        for (sender in senders) {
            sender.send(envelope)
        }
    }

    private fun retrieveSenders(messageType: String): List<SenderInterface> =
        senderRouting[messageType] ?: throw RuntimeException("Cannot find routing for message type $messageType")

    private fun setRoutingSender(messageType: String, sender: SenderInterface) {
        if (!senderRouting.containsKey(messageType)) {
            senderRouting[messageType] = arrayListOf(sender)

            return
        }

        senderRouting[messageType]!!.add(sender)
    }

    private fun getTransportByName(name: String, transports: Array<TransportConfig>): TransportConfig {
        for (transport in transports) {
            if (transport.name == name) {
                return transport
            }
        }

        throw RuntimeException("Cannot find transport $name")
    }
}
