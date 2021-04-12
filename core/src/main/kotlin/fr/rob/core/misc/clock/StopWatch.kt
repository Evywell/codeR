package fr.rob.core.misc.clock

class StopWatch {

    private var startTime: Long? = null
    private var stopTime: Long? = null

    fun start() {
        startTime = System.currentTimeMillis()
    }

    fun stop() {
        stopTime = System.currentTimeMillis()
    }

    fun diffTime(): Long {
        return stopTime!! - startTime!!
    }
}
