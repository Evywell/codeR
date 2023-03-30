package fr.rob.game.domain.event

/**
 * @todo improve by using generics
 */
interface DomainEventListenerInterface {
    fun supports(event: DomainEventInterface): Boolean
    fun invoke(event: DomainEventInterface)
}
