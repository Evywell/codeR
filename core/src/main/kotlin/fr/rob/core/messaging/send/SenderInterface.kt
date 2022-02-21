package fr.rob.core.messaging.send

import fr.rob.core.entities.MessagingProto

interface SenderInterface {
    fun send(envelope: MessagingProto.Envelope)
}
