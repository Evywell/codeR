package fr.rob.core.misc.clock

class IntervalTimer(private var intervalMs: Int, var localTime: Int = 0) {

    fun update(delta: Int) {
        localTime += delta

        if (localTime < 0) {
            localTime = 0
        }
    }

    fun passed(): Boolean {
        return localTime >= intervalMs
    }

    fun reset() {
        if (localTime >= intervalMs) {
            localTime %= intervalMs
        }
    }
}
