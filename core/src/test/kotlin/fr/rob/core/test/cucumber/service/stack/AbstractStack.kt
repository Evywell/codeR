package fr.rob.core.test.cucumber.service.stack

abstract class AbstractStack {

    var isReady = false

    private val process: StackProcess by lazy { StackProcess(this) }

    fun start() {
        Thread(process).start()
    }

    fun shutdown() {
        process.isRunning = false
        process.items.clear()
    }

    fun pushItem(item: StackItem) {
        process.items.addLast(item)
    }

    protected abstract fun processItem(item: StackItem)

    class StackProcess(private val stack: AbstractStack) : Runnable {

        var isRunning = false
        val items = ArrayDeque<StackItem>()

        override fun run() {
            stack.isReady = true
            isRunning = true
            try {
                while (isRunning) {
                    // Do something
                    val item = items.firstOrNull()

                    if (item != null) {
                        stack.processItem(item)
                        items.removeFirst()
                    }

                    Thread.sleep(10)
                }
            } catch (e: Exception) {
                println(e.stackTraceToString())
            }
            stack.isReady = false
        }
    }
}
