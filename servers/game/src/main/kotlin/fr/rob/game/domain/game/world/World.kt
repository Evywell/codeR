package fr.rob.game.domain.game.world

import fr.rob.core.misc.clock.IntervalTimer

class World {

    private val welcomeTimer = IntervalTimer(1000)

    fun initialize() { }

    fun loop() {
        var realCurrentTime: Long
        var realPreviousTime = System.currentTimeMillis()
        var deltaTime: Long
        var executionTime: Long

        while (true) {
            realCurrentTime = System.currentTimeMillis()

            deltaTime = realCurrentTime - realPreviousTime

            update(deltaTime.toInt())

            realPreviousTime = realCurrentTime
            executionTime = System.currentTimeMillis() - realCurrentTime

            if (executionTime < WORLD_UPDATE_INTERVAL) {
                Thread.sleep(WORLD_UPDATE_INTERVAL - executionTime)
            }
        }
    }

    private fun update(deltaTime: Int) {
        if (welcomeTimer.localTime >= 0) {
            welcomeTimer.update(deltaTime)
        } else {
            welcomeTimer.reset()
        }

        if (welcomeTimer.passed()) {
            println("Welcome")
            welcomeTimer.reset()
        }
    }

    companion object {
        private const val WORLD_UPDATE_PER_SECOND = 50
        const val WORLD_UPDATE_INTERVAL = 1000/WORLD_UPDATE_PER_SECOND
    }
}
