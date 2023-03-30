package fr.rob.game.domain.event

class DomainEventContainer : DomainEventCarrierInterface {
    private val events = ArrayList<DomainEventInterface>()

    override fun pushEvent(event: DomainEventInterface) {
        events.add(event)
    }

    override fun getDomainEventContainer(): Collection<DomainEventInterface> = events

    fun resetContainer() {
        events.clear()
    }
}
