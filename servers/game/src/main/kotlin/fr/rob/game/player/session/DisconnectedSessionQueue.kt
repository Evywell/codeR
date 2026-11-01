package fr.rob.game.player.session

import java.util.concurrent.ConcurrentLinkedQueue

/**
 * Hands sessions whose connection was lost from the network threads over to the world thread.
 */
class DisconnectedSessionQueue {
    private val sessions = ConcurrentLinkedQueue<GameSession>()

    fun enqueue(session: GameSession) {
        sessions.add(session)
    }

    fun poll(): GameSession? = sessions.poll()
}
