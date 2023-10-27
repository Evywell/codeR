package fr.rob.game.domain.world

class WorldUpdater(private val world: World, private val updatableObjects: Array<UpdatableInterface>) {

    private var isRunning: Boolean = false
    var deltaTime: Int = 0
        private set

    fun initialize() {
        isRunning = true
    }

    fun loop() {
        var realCurrentTime: Long
        var realPreviousTime = System.currentTimeMillis()
        var executionTime: Long

        while (isRunning) {
            realCurrentTime = System.currentTimeMillis()

            deltaTime = (realCurrentTime - realPreviousTime).toInt()

            world.update(deltaTime)
            updatableObjects.forEach { it.update(deltaTime) }

            realPreviousTime = realCurrentTime
            executionTime = System.currentTimeMillis() - realCurrentTime

            if (executionTime < WORLD_UPDATE_INTERVAL) {
                Thread.sleep(WORLD_UPDATE_INTERVAL - executionTime)
            }
        }
    }

    companion object {
        const val WORLD_UPDATE_PER_SECOND = 50
        const val WORLD_UPDATE_INTERVAL = 1000 / WORLD_UPDATE_PER_SECOND
    }
}
