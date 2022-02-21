package fr.rob.core.messaging.rabbitmq

import com.rabbitmq.client.Channel
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.DeliverCallback
import com.rabbitmq.client.Delivery
import fr.rob.core.log.LoggerInterface
import fr.rob.core.messaging.rabbitmq.exception.ExceptionHandler

class AMQPConnection(val config: AMQPConfig, val logger: LoggerInterface) {
    private val channel: Channel
    private val queues = ArrayList<String>()

    init {
        val factory = ConnectionFactory()
        factory.setUri("amqp://${config.username}:${config.password}@${config.hostname}:${config.port}/${config.vhost}")
        factory.exceptionHandler = ExceptionHandler(logger)

        val connection = factory.newConnection()
        channel = connection.createChannel()
    }

    fun publishInQueue(queueName: String, body: ByteArray) {
        createQueueIfNotExist(queueName)

        channel.basicPublish("", queueName, null, body)
    }

    fun consumeQueue(queueName: String, callback: (body: ByteArray) -> Unit) {
        createQueueIfNotExist(queueName)

        val deliverCallback = DeliverCallback { _, delivery: Delivery ->
            callback(delivery.body)
        }

        channel.basicConsume(queueName, true, deliverCallback) { _ -> }
    }

    private fun createQueueIfNotExist(queueName: String) {
        if (queues.contains(queueName)) {
            return
        }

        channel.queueDeclare(queueName, false, false, false, null)
        queues.add(queueName)
    }
}
