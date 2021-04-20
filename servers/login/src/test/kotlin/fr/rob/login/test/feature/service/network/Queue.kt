package fr.rob.login.test.feature.service.network

import fr.rob.core.network.Packet

abstract class Queue: Thread() {

    private var isRunning = false

    private val itemList = ArrayDeque<QueueItem>()

    private var realCurrentTime: Long? = null
    private var realPreviousTime = System.currentTimeMillis()

    protected var deltaTime: Long? = null

    protected abstract fun runItem(item: QueueItem?)

    fun add(item: QueueItem) {
        itemList.addLast(item)
    }

    override fun run() {
        isRunning = true

        while(isRunning) {
            realCurrentTime = System.currentTimeMillis()

            deltaTime = realCurrentTime!! - realPreviousTime

            val item = itemList.firstOrNull()

            runItem(item)

            itemList.removeFirstOrNull()

            realPreviousTime = realCurrentTime as Long

            sleep(100)
        }
    }

    fun shutdown() {
        isRunning = false
    }
}

open class QueueItem (
    open val opcode: Int,
    open val packet: Packet
)
