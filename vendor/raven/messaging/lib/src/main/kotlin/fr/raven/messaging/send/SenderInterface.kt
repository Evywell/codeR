package fr.raven.messaging.send

import fr.raven.messaging.message.MessagingProto

interface SenderInterface {
    fun send(envelope: MessagingProto.Envelope)
}
