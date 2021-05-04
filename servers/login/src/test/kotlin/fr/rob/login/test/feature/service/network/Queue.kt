package fr.rob.login.test.feature.service.network

import fr.rob.core.network.Packet
import fr.rob.login.test.feature.service.exception.ExceptionHolder

abstract class Queue {

    private lateinit var queueProcess: QueueProcess


    private var isRunning = false

    private var realCurrentTime: Long? = null
    private var realPreviousTime = System.currentTimeMillis()

    protected var deltaTime: Long? = null

    var isShutdownSuccessfully = false
        private set

    private var isShutdownScheduled = false

    protected abstract fun runItem(item: QueueItem?)

    fun add(item: QueueItem) {
        queueProcess.itemList.addLast(item)
    }

    fun start() {
        queueProcess = QueueProcess(this)

        Thread(queueProcess).start()
    }

    fun shutdown() {
        this.isShutdownScheduled = true
    }

    companion object {
        private const val QUEUE_UPDATE_PER_SECOND = 50
        const val QUEUE_UPDATE_INTERVAL = 1000/QUEUE_UPDATE_PER_SECOND
    }

    class QueueProcess(private val queue: Queue): Runnable {

        val itemList = ArrayDeque<QueueItem>()

        private var isRunning: Boolean
            get() = queue.isRunning
            set(value) { queue.isRunning = value }

        private val isShutdownScheduled: Boolean
            get() = queue.isShutdownScheduled

        private var realCurrentTime: Long?
            get() = queue.realCurrentTime
            set(value) { queue.realCurrentTime = value }

        private var deltaTime: Long?
            get() = queue.deltaTime
            set(value) { queue.deltaTime = value }

        private var realPreviousTime: Long
            get() = queue.realPreviousTime
            set(value) { queue.realPreviousTime = value }

        override fun run() {
            try {
                var executionTime: Long

                isRunning = true

                while(isRunning) {
                    if (isShutdownScheduled) {
                        isRunning = false
                        queue.isShutdownSuccessfully = true
                        break
                    }

                    realCurrentTime = System.currentTimeMillis()

                    deltaTime = realCurrentTime!! - realPreviousTime

                    val item = itemList.firstOrNull()

                    queue.runItem(item)

                    itemList.removeFirstOrNull()

                    realPreviousTime = realCurrentTime as Long

                    executionTime = System.currentTimeMillis() - realCurrentTime!!

                    if (executionTime < QUEUE_UPDATE_INTERVAL) {
                        Thread.sleep(QUEUE_UPDATE_INTERVAL - executionTime)
                    }
                }
            } catch (e: Exception) {
                ExceptionHolder.throwException(e)
            }
        }

    }
}

open class QueueItem (
    open val opcode: Int,
    open val packet: Packet
)
