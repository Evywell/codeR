package fr.rob.core.messaging.send

import com.google.protobuf.Message

interface MessageQueueDispatcherInterface {
    fun dispatch(message: Message)
}
