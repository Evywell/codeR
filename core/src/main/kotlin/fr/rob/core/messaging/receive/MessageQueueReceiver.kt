package fr.rob.core.messaging.receive

import fr.rob.core.entities.MessagingProto
import fr.rob.core.log.LoggerInterface

class MessageQueueReceiver(receivers: Array<ReceiverInterface>, private val logger: LoggerInterface) :
    MessageQueueReceiverInterface {
    private val handlers = HashMap<String, MutableList<MessageHandlerInterface>>()

    init {
        for (receiver in receivers) {
            receiver.registerQueue(this)
        }
    }

    override fun attachHandler(eventName: String, handler: MessageHandlerInterface) {
        if (!handlers.containsKey(eventName)) {
            handlers[eventName] = ArrayList()
        }

        handlers[eventName]?.add(handler)
    }

    override fun handleMessage(envelope: MessagingProto.Envelope) {
        val eventName = envelope.eventName
        logger.info("Message received for event $eventName")

        handlers[eventName]?.forEach { handler ->
            logger.debug("Handling message ${handler.javaClass.name} for event ${envelope.eventName}")
            handler.handle(envelope)
        }
    }
}
