package fr.rob.core.messaging.rabbitmq

import fr.rob.core.entities.MessagingProto
import fr.rob.core.messaging.send.SenderInterface

class AMQPSender(private val queueName: String, val connection: AMQPConnection) : SenderInterface {

    override fun send(envelope: MessagingProto.Envelope) {
        connection.publishInQueue(queueName, envelope.toByteArray())
    }
}
