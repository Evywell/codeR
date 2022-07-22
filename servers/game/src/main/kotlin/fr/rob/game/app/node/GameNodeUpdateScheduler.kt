package fr.rob.game.app.node

import fr.rob.game.domain.instance.InstanceManager

class GameNodeUpdateScheduler(private val instanceManager: InstanceManager) {

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

            if (executionTime < NODE_UPDATE_INTERVAL) {
                Thread.sleep(NODE_UPDATE_INTERVAL - executionTime)
            }
        }
    }

    private fun update(deltaTime: Int) {
        instanceManager.update(deltaTime)
    }

    companion object {
        private const val NODE_UPDATE_PER_SECOND = 50
        const val NODE_UPDATE_INTERVAL = 1000 / NODE_UPDATE_PER_SECOND
    }
}
