package fr.rob.orchestrator.api.node

import fr.rob.core.entities.MessagingProto
import fr.rob.core.messaging.receive.MessageHandlerInterface

class GameNodeCreatedHandler : MessageHandlerInterface {
    override fun handle(envelope: MessagingProto.Envelope) {
        println("Super")
    }
}
