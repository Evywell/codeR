package fr.rob.game.infra.event

import fr.rob.game.domain.event.DomainEventDispatcherInterface
import fr.rob.game.domain.event.DomainEventInterface
import fr.rob.game.domain.event.DomainEventListenerInterface

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
