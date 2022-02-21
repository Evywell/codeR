package fr.rob.core.messaging.receive

interface ReceiverInterface {
    fun registerQueue(queueReceiver: MessageQueueReceiverInterface)
}
