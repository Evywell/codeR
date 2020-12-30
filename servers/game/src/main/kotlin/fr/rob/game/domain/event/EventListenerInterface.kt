package fr.rob.game.domain.event

interface EventListenerInterface {

    fun process(event: EventInterface)
}
