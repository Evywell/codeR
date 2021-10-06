package fr.rob.game.network.event

import fr.rob.core.event.EventInterface
import fr.rob.core.network.Server

class GameNodeStarted(val server: Server, override var propagationStopped: Boolean? = false) : EventInterface {

    override fun getName(): String = GAME_NODE_STARTED_EVENT

    companion object {
        const val GAME_NODE_STARTED_EVENT = "GameNodeStartedEvent"
    }
}
