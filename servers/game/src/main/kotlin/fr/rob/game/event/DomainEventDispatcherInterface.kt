package fr.rob.game.event

interface DomainEventDispatcherInterface {
    fun dispatch(event: DomainEventInterface)
    fun attachListener(listener: DomainEventListenerInterface)
}
