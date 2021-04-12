package fr.rob.core.event

open class Event(override var propagationStopped: Boolean? = false) : EventInterface {

    override fun getName(): String = DEFAULT_EVENT_NAME

    companion object {
        const val DEFAULT_EVENT_NAME = "Default"
    }
}
