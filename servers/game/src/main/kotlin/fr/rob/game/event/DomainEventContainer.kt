package fr.rob.game.event

class DomainEventContainer : DomainEventCarrierInterface {
    private val events = ArrayList<DomainEventInterface>()
    private val uniqueEvents = ArrayList<String>()

    override fun pushEvent(event: DomainEventInterface) {
        if (event is UniqueDomainEventInterface) {
            val eventKey = event::class.qualifiedName
            uniqueEvents.forEach { if (it == eventKey) return }

            uniqueEvents.add(eventKey!!)
        }

        events.add(event)
    }

    override fun getDomainEventContainer(): Collection<DomainEventInterface> = events

    fun resetContainer() {
        events.clear()
        uniqueEvents.clear()
    }
}
