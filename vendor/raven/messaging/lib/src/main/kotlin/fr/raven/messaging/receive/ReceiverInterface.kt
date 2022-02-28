package fr.raven.messaging.receive

interface ReceiverInterface {
    fun registerQueue(queueReceiver: MessageQueueReceiverInterface)
}
