package fr.rob.core.messaging.receive

import fr.rob.core.entities.MessagingProto

interface MessageQueueReceiverInterface {
    fun attachHandler(eventName: String, handler: MessageHandlerInterface)
    fun handleMessage(envelope: MessagingProto.Envelope)
}
