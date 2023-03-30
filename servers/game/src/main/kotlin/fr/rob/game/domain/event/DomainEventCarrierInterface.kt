package fr.rob.game.domain.event

interface DomainEventCarrierInterface {
    fun pushEvent(event: DomainEventInterface)
    fun getDomainEventContainer(): Collection<DomainEventInterface>
}
