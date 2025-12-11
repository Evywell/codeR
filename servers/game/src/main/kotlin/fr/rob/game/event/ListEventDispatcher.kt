package fr.rob.game.event

class ListEventDispatcher : DomainEventDispatcherInterface {
    private val listeners = ArrayList<DomainEventListenerInterface>()

    override fun dispatch(event: DomainEventInterface) {
        listeners.forEach { listener ->
            if (listener.supports(event)) {
                listener.invoke(event)
            }
        }
    }

    override fun attachListener(listener: DomainEventListenerInterface) {
        listeners.add(listener)
    }
}
