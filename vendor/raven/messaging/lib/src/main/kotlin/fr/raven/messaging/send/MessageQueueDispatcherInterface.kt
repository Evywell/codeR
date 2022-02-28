package fr.raven.messaging.send

import com.google.protobuf.Message

interface MessageQueueDispatcherInterface {
    fun dispatch(message: Message)
}
