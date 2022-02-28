package fr.raven.messaging.receive

import fr.raven.messaging.message.MessagingProto

interface MessageQueueReceiverInterface {
    fun attachHandler(eventName: String, handler: MessageHandlerInterface)
    fun handleMessage(envelope: MessagingProto.Envelope)
}
