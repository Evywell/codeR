package fr.raven.messaging.rabbitmq

import fr.raven.messaging.message.MessagingProto
import fr.raven.messaging.receive.MessageQueueReceiverInterface
import fr.raven.messaging.receive.ReceiverInterface

class AMQPReceiver(private val queueName: String, private val connection: AMQPConnection) : ReceiverInterface {
    override fun registerQueue(queueReceiver: MessageQueueReceiverInterface) {
        connection.consumeQueue(queueName) { body: ByteArray ->
            val envelope = MessagingProto.Envelope.parseFrom(body)

            queueReceiver.handleMessage(envelope)
        }
    }
}
