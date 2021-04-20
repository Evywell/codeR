package fr.rob.login.test.feature.service.network

import fr.rob.core.network.Packet
import java.util.ArrayList

class ClientQueue(private val client: Client, private val incomingMessageListeners: MutableMap<Int, Listener>): Queue() {

    override fun runItem(item: QueueItem?) {
        var msg: Any? = null

        if (item != null) {
            item as ClientQueueItem
            msg = client.processMessage(item.opcode, item.packet)
        }

        val deletableListeners = ArrayList<Int>()

        for ((id, listener) in incomingMessageListeners) {
            if (listener.isDone) {
                deletableListeners.add(id)

                continue
            }

            deltaTime?.let { listener.update(it.toInt()) }
            item?.let { listener.call(it.opcode, it.packet, msg) }
        }

        for (id in deletableListeners) {
            incomingMessageListeners.remove(id)
        }
    }
}

data class ClientQueueItem(override val opcode: Int, override val packet: Packet):
    QueueItem(opcode, packet)
