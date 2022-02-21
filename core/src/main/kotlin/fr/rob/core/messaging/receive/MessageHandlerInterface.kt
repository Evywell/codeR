package fr.rob.core.messaging.receive

import fr.rob.core.entities.MessagingProto

interface MessageHandlerInterface {
    fun handle(envelope: MessagingProto.Envelope)
}
