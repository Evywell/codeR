package fr.rob.core.messaging.rabbitmq

import fr.rob.core.entities.MessagingProto
import fr.rob.core.messaging.receive.MessageQueueReceiverInterface
import fr.rob.core.messaging.receive.ReceiverInterface

class AMQPReceiver(private val queueName: String, private val connection: AMQPConnection) : ReceiverInterface {
    override fun registerQueue(queueReceiver: MessageQueueReceiverInterface) {
        connection.consumeQueue(queueName) { body: ByteArray ->
            val envelope = MessagingProto.Envelope.parseFrom(body)

            queueReceiver.handleMessage(envelope)
        }
    }
}
