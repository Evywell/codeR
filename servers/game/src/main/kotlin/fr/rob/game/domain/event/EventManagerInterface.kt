package fr.rob.game.domain.event

interface EventManagerInterface {

    fun dispatch(event: EventInterface)
    fun dispatch(eventName: String, event: EventInterface)
    fun addEventListener(eventName: String, listener: EventListenerInterface)
}
