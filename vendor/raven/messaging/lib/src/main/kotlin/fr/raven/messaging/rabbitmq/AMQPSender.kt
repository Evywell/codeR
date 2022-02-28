package fr.raven.messaging.rabbitmq

import fr.raven.messaging.message.MessagingProto
import fr.raven.messaging.send.SenderInterface

class AMQPSender(private val queueName: String, private val connection: AMQPConnection) : SenderInterface {

    override fun send(envelope: MessagingProto.Envelope) {
        connection.publishInQueue(queueName, envelope.toByteArray())
    }
}
