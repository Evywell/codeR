package fr.raven.messaging.test.receive

import fr.raven.log.NullLogger
import fr.raven.messaging.message.MessagingProto.Envelope
import fr.raven.messaging.receive.MessageHandlerInterface
import fr.raven.messaging.receive.MessageQueueReceiver
import fr.raven.messaging.receive.MessageQueueReceiverInterface
import fr.raven.messaging.receive.ReceiverInterface
import org.junit.jupiter.api.Test
import org.mockito.kotlin.spy
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

class MessageQueueReceiverTest {
    @Test
    fun `when receiving a message, the handler should be called`() {
        // Arrange
        val receiver = MessageQueueReceiver(arrayOf(DummyReceiver()), NullLogger())
        val handler = DummyHandler()
        val handlerSpy = spy(handler)
        val envelope = Envelope.newBuilder()
            .setEventName("event_name")
            .build()

        // Act
        receiver.attachHandler("event_name", handlerSpy)
        receiver.handleMessage(envelope)

        // Assert
        verify(handlerSpy, times(1)).handle(envelope)
    }

    @Test
    fun `when receiving a message, all the linked handlers should be called`() {
        // Arrange
        val receiver = MessageQueueReceiver(arrayOf(DummyReceiver()), NullLogger())

        val handler1 = DummyHandler()
        val handlerSpy1 = spy(handler1)

        val handler2 = DummyHandler()
        val handlerSpy2 = spy(handler2)

        val envelope = Envelope.newBuilder()
            .setEventName("event_name")
            .build()

        // Act
        receiver.attachHandler("event_name", handlerSpy1)
        receiver.attachHandler("event_name", handlerSpy2)
        receiver.handleMessage(envelope)

        // Assert
        verify(handlerSpy1, times(1)).handle(envelope)
        verify(handlerSpy2, times(1)).handle(envelope)
    }

    @Test
    fun `handler should only be called if its the receiver`() {
        // Arrange
        val receiver = MessageQueueReceiver(arrayOf(DummyReceiver()), NullLogger())
        val handler = DummyHandler()
        val handlerSpy = spy(handler)
        val envelope = Envelope.newBuilder()
            .setEventName("event_name")
            .build()

        // Act
        receiver.attachHandler("another_event_name", handlerSpy)
        receiver.handleMessage(envelope)

        // Assert
        verify(handlerSpy, times(0)).handle(envelope)
    }

    class DummyReceiver : ReceiverInterface {
        override fun registerQueue(queueReceiver: MessageQueueReceiverInterface) {}
    }

    open class DummyHandler : MessageHandlerInterface {
        override fun handle(envelope: Envelope) {}
    }
}
