package fr.rob.login.test.feature.service.network

import fr.rob.core.network.Packet

abstract class Queue: Thread() {

    private var isRunning = false

    private val itemList = ArrayDeque<QueueItem>()

    private var realCurrentTime: Long? = null
    private var realPreviousTime = System.currentTimeMillis()

    protected var deltaTime: Long? = null

    var isShutdownSuccessfully = false
        private set

    private var isShutdownScheduled = false

    protected abstract fun runItem(item: QueueItem?)

    fun add(item: QueueItem) {
        itemList.addLast(item)
    }

    override fun run() {
        var executionTime: Long

        isRunning = true

        while(isRunning) {
            realCurrentTime = System.currentTimeMillis()

            deltaTime = realCurrentTime!! - realPreviousTime

            val item = itemList.firstOrNull()

            runItem(item)

            itemList.removeFirstOrNull()

            realPreviousTime = realCurrentTime as Long

            executionTime = System.currentTimeMillis() - realCurrentTime!!

            if (executionTime < QUEUE_UPDATE_INTERVAL) {
                sleep(QUEUE_UPDATE_INTERVAL - executionTime)
            }

            if (isShutdownScheduled) {
                isRunning = false
                isShutdownSuccessfully = true
            }
        }
    }

    fun shutdown() {
        isShutdownScheduled = true
    }

    companion object {
        private const val QUEUE_UPDATE_PER_SECOND = 50
        const val QUEUE_UPDATE_INTERVAL = 1000/QUEUE_UPDATE_PER_SECOND
    }
}

open class QueueItem (
    open val opcode: Int,
    open val packet: Packet
)
