package fr.rob.game.test.unit.tools

import fr.rob.game.domain.event.DomainEventDispatcherInterface
import fr.rob.game.domain.event.DomainEventInterface
import fr.rob.game.domain.event.DomainEventListenerInterface

class VoidEventDispatcher : DomainEventDispatcherInterface {
    override fun dispatch(event: DomainEventInterface) {}

    override fun attachListener(listener: DomainEventListenerInterface) {}
}
