package fr.rob.game.domain.event

interface DomainEventDispatcherInterface {
    fun dispatch(event: DomainEventInterface)
    fun attachListener(listener: DomainEventListenerInterface)
}
