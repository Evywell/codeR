package fr.raven.messaging.receive

import fr.raven.messaging.message.MessagingProto

interface MessageHandlerInterface {
    fun handle(envelope: MessagingProto.Envelope)
}
