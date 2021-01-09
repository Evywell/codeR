package fr.rob.game.infrastructure.event

import fr.rob.game.domain.event.EventInterface

open class Event(override var propagationStopped: Boolean? = false) : EventInterface {

    override fun getName(): String = DEFAULT_EVENT_NAME

    companion object {
        const val DEFAULT_EVENT_NAME = "Default"
    }
}
