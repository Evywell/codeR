package fr.raven.messaging.test.send

import fr.raven.messaging.message.MessagingProto
import fr.raven.messaging.send.MessageQueueDispatcher
import fr.raven.messaging.send.QueueRouting
import fr.raven.messaging.send.SenderInterface
import fr.raven.messaging.send.TransportConfig
import fr.raven.messaging.test.message.MessagingTestProto
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.spy
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

class MessageQueueDispatcherTest {
    @Test
    fun `dispatched message is correctly transmitted`() {
        // Arrange
        val sender = NullSender()
        val spySender = spy(sender)
        val transport = TransportConfig("testing_transport", spySender)
        val routing = QueueRouting(MessagingTestProto.Null::class.java.name, "testing_transport")

        val dispatcher = MessageQueueDispatcher(arrayOf(transport), arrayOf(routing))

        // Act
        dispatcher.dispatch(MessagingTestProto.Null.newBuilder().build())

        // Assert
        verify(spySender, times(1)).send(any())
    }

    @Test
    fun `dispatched message is transmitted by the right sender`() {
        // Arrange
        val sender1 = NullSender()
        val spySender1 = spy(sender1)
        val transport1 = TransportConfig("testing_transport1", spySender1)

        val sender2 = NullSender()
        val spySender2 = spy(sender2)
        val transport2 = TransportConfig("testing_transport2", spySender2)

        val routing = QueueRouting(MessagingTestProto.Null::class.java.name, "testing_transport1")

        val dispatcher = MessageQueueDispatcher(arrayOf(transport1, transport2), arrayOf(routing))

        // Act
        dispatcher.dispatch(MessagingTestProto.Null.newBuilder().build())

        // Assert
        verify(spySender1, times(1)).send(any())
        verify(spySender2, times(0)).send(any())
    }

    @Test
    fun `dispatched message is correctly transmitted to all the compatible senders`() {
        // Arrange
        val sender = NullSender()
        val spySender = spy(sender)
        val transport1 = TransportConfig("testing_transport1", spySender)
        val transport2 = TransportConfig("testing_transport2", spySender)
        val routing1 = QueueRouting(MessagingTestProto.Null::class.java.name, "testing_transport1")
        val routing2 = QueueRouting(MessagingTestProto.Null::class.java.name, "testing_transport2")

        val dispatcher = MessageQueueDispatcher(arrayOf(transport1, transport2), arrayOf(routing1, routing2))

        // Act
        dispatcher.dispatch(MessagingTestProto.Null.newBuilder().build())

        // Assert
        verify(spySender, times(2)).send(any())
    }

    @Test
    fun `transport should not be called if not message are sent`() {
        // Arrange
        val sender = NullSender()
        val spySender = spy(sender)
        val transport = TransportConfig("testing_transport", spySender)
        val routing1 = QueueRouting(MessagingTestProto.Null::class.java.name, "testing_transport")
        val routing2 = QueueRouting("NotingHere", "testing_transport")

        val dispatcher = MessageQueueDispatcher(arrayOf(transport), arrayOf(routing1, routing2))

        // Act
        dispatcher.dispatch(MessagingTestProto.Null.newBuilder().build())

        // Assert
        verify(spySender, times(1)).send(any())
    }

    @Test
    fun `dispatcher should throw an exception when trying to dispatch a non routed message`() {
        // Arrange
        val dispatcher = MessageQueueDispatcher(arrayOf(), arrayOf())

        // Act & Assert
        Assertions.assertThrows(RuntimeException::class.java) {
            dispatcher.dispatch(MessagingTestProto.Null.newBuilder().build())
        }
    }

    @Test
    fun `construct the dispatcher with a routing linked to an unknown transport should throw an exception`() {
        // Arrange
        val routing = QueueRouting(MessagingTestProto.Null::class.java.name, "testing_transport")

        // Act & Assert
        Assertions.assertThrows(RuntimeException::class.java) {
            MessageQueueDispatcher(arrayOf(), arrayOf(routing))
        }
    }

    open class NullSender : SenderInterface {
        override fun send(envelope: MessagingProto.Envelope) {}
    }
}
