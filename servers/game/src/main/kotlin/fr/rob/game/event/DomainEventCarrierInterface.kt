package fr.rob.game.event

interface DomainEventCarrierInterface {
    fun pushEvent(event: DomainEventInterface)
    fun getDomainEventContainer(): Collection<DomainEventInterface>
}
