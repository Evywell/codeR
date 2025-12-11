package fr.rob.game.event

/**
 * @todo improve by using generics
 */
interface DomainEventListenerInterface {
    fun supports(event: DomainEventInterface): Boolean
    fun invoke(event: DomainEventInterface)
}
