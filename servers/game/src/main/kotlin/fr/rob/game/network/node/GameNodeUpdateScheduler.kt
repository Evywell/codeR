package fr.rob.game.network.node

import fr.rob.game.game.world.instance.InstanceManager
import fr.rob.game.network.GameNodeServer
import fr.rob.game.network.GamePacketFilter
import fr.rob.game.network.session.GameSession

class GameNodeUpdateScheduler(private val server: GameNodeServer, private val instanceManager: InstanceManager) {

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

        updateSessions(deltaTime)
    }

    private fun updateSessions(deltaTime: Int) {
        for ((_, session) in server.getAllSessions()) {
            session as GameSession
            session.update(deltaTime, GamePacketFilter(session))
        }
    }

    companion object {
        private const val NODE_UPDATE_PER_SECOND = 50
        const val NODE_UPDATE_INTERVAL = 1000 / NODE_UPDATE_PER_SECOND
    }
}
